package AutoTest;

use strict;
use warnings;
use JSON;
use utf8 ':all';
use Encode;
use Data::Dumper;
use Getopt::Long;

use constant debug => false;

binmode STDERR, ':utf8';
binmode STDOUT, ':utf8';

sub new {
    my $pkg = shift;
    my $r = { @_ };
    unless ($r->{host}) {
        die "must provide host";
    }
    unless ($r->{system}) {
        die "must provide system name";
    }
    bless $r, $pkg;
}

# 发送post
# 如果成功返回data部分, 如果错误, 打印error并退出
# $agent->pos($name, $url, { jwt => "jwt", data => json })
sub pos {
    my $self = shift;
    my $name = shift;
    my $url = shift;
    my $args = shift;

    my $json = encode_json($args->{data});
    # warn "send json[$json]";

    my $cmd;
    if ($args->{jwt}) {
        $cmd = "curl -s -H \"Authorization: Bearer $self->{jwt}\"  --cookie \"$self->{c}\" -XPOST -H \"Content-Type: application/json\" \"http://$self->{host}$url\" -d \'$json\'";
    } else {
        $cmd = "curl -s --cookie \"$self->{c}\" -XPOST -H \"Content-Type: application/json\" \"http://$self->{host}$url\" -d \'$json\'";
    }
    my $response = `$cmd`;
    if ($? != 0) {
        warn sprintf("%s - 系统失败[$cmd]\n", $name);
        exit 0;
    } else {
        warn "response = $response" if debug;
        my $res = decode_json($response);
        if ($res->{success}) {
            warn sprintf("%s - 成功\n", $name);
            return $res->{data};
        } else {
            warn sprintf("%s - 失败[%s]\n", $name, $res->{error}->{message});
            exit 0;
        }
    }
}

# 发送get
# 如果成功返回data部分, 如果错误, 打印error并退出
# $agent->get($name, $url, {jwt => kkk)
sub get {
    my $self = shift;
    my $name = shift;
    my $url = shift;
    my $args = shift;

    my $cmd;
    if ($args->{jwt}) {
        $cmd = "curl -s -H \"Authorization: Bearer $self->{jwt}\" --cookie-jar target/$self->{system}.cookie --cookie \"$self->{c}\" -XGET \"http://$self->{host}$url\"";
    } else {
        $cmd = "curl -s --cookie-jar target/$self->{system}.cookie --cookie \"$self->{c}\" -XGET \"http://$self->{host}$url\"";
    }

    my $response = `$cmd`;

    #    if ( $url =~ /captcha\/find/) {
    #       warn "captch find response = $response";
    #    }

    if ($? != 0) {
        die sprintf("%s - 系统失败\n", $name);
    } else {
        # 如果是触发生成图形验证码, 则不返回
        if ($name =~ "触发服务器生成图形验证码") {
            warn sprintf("%s - 成功\n", $name);
            return;
        }
        if ($url =~ /\/web\//) {
            warn sprintf("%s - 成功\n", $name);
            return;
        }

        my $res;
        eval {
            $res = decode_json($response);
        };
        if ($@) {
            die sprintf("%s - 失败, response=[$s]\n", $name, $response);
        }

        if ($res->{success}) {
            warn sprintf("%s - 成功\n", $name);
            return $res->{data};
        } else {
            die sprintf("%s - 失败[%s]\n", $name, $res->{error}->{message});
        }

    }
}

# 清空系统
sub clear {
    my $self = shift;
    # 1. 清空所有数据库
    warn "-----------------------------------------------\n";
    warn "清空系统";
    warn "-----------------------------------------------\n";
    my $cmd = <<EOF;
echo "db.user.drop(); db.company.drop(); db.userinfo.drop();" |  mongo
EOF
    system($cmd);
}

# 注册所有用户
sub register {
    my $self = shift;
    my ($phone, $userName, $loginName, $password) = @_;
    my $system = $self->{system};
    warn "$system 注册...\n";
    my $u000 = $self->get("0. 打开注册页面", "/web/$system/register");
    $self->cookie(); # 设置cookie
    my $u001 = $self->get("1. 触发服务器生成图形验证码", "/api/$system/captcha/get");
    my $u002 = $self->get("2. 获取图形验证码", "/api/$system/captcha/find");
    my $u003 = $self->get("3. 触发服务器发送短信验证码", "/api/$system/register/sms?mobilePhone=$phone&imgCode=$u002");
    my $u004 = $self->get("4. 获取短信验证码", "/api/$system/register/findSms?authCodeType=REGISTER");
    my $u005 = $self->pos("5. 发起注册", "/api/$system/register/register", {
            data => {
                "userName"  => $userName,
                "loginName" => $loginName,
                "password"  => $password,
                "phone"     => $phone,
                "smsCode"   => $u004,   # 短信验证码
            }
        });
}

sub login {
    my $self = shift;
    my ($username, $password) = @_;
    my $system = $self->{system};
    warn "-----------------------------------------------\n";
    warn "$system 登录...\n";
    warn "-----------------------------------------------\n";
    my $u000 = $self->get("0. 打开登录页面", "/web/$system/login");
    $self->cookie(); # 设置cookie
    my $u001 = $self->pos("1. 登录", "/api/$system/login/jwt", {
            jwt  => undef, # 不需要jwt
            data => {
                username => $username,
                password => $password,
            }
        });
    $self->jwt($u001); # 保存jwt到agent中
}

sub add_company{
    my $self = shift;
    my ($companyName) = @_;
    warn "-----------------------------------------------\n";
    warn "$system 添加公司开始...\n";
    warn "-----------------------------------------------\n";
    my $u001 = $self->pos("1. 保存公司信息", "/api/$self->{system}/identify/saveCompany", {
            jwt  => 1,
            data => {
                "companyName"         => $companyName,
                "companyType"         => 1,
                "registerCapital"     => 323233,
                "scope"               => "xxxxxxx",
                "founded"             => "2017-12-12",
                "province"            => "河南省",
                "city"                => "焦作市",
                "district"            => "孟州市",
                "license"=>{
                    "operationType"       => 1,
                    "operationBegin"      => "2017-12-12",
                    "operationEnd"        => "2017-12-12",
                    "lastCheck"           => "2017-12-12",
                    "registerOrgan"       => "工商局",
                    "unionLicenseCode"    => "23423424242342",
                    "operationLicensePic" => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    },
                    "organLicensePic"     => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    },
                    "unionLicensePic"     => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    },
                    "bankLicensePic"      => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    }
                },
                "legalPerson"         => {
                    "legalPersonName"             => "$self->{system}-法人",
                    "legalPersonId"               => "32324234",
                    "legalPersonPhone"            => "2342343423424",
                    "legalPersonIdFrontSidePic"   => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    },
                    "legalPersonIdReverseSidePic" => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    },
                },
                "authPerson"          => {
                    "authPersonName"             => "$self->{system}-代理人",
                    "authPersonId"               => "32324234",
                    "authPersonPhone"            => "2342343423424",
                    "authDocumentPic"            => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    },
                    "authPersonIdFrontSidePic"   => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    },
                    "authPersonIdReverseSidePic" => {
                        "filePath"    => "/werwerewrrwr",
                        "fileName"    => "werwerewrrwr.pdf",
                        "fileExtName" => "pdf"
                    },
                }
            }
        });
}

sub dump {
    my $self = shift;
    print Dumper(@_);
}

sub cookie {
    my $self = shift;
    my $cookie = `cat target/$self->{system}.cookie | grep $self->{system} | awk '{ print \$7;}'`;
    chomp $cookie;
    $self->{c} = "$self->{system}=$cookie";
}

sub jwt {
    my $self = shift;
    my $jwt = shift;
    $self->{jwt} = $jwt;
}



1;

__END__
