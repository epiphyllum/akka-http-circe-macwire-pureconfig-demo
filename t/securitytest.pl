#!/usr/bin/perl
use strict;
use lib "t/lib";
use AutoTest;
use JSON;
use utf8 ':all';
use Encode;
use Data::Dumper;
use Getopt::Long;

my $upstream = AutoTest->new(host => "127.0.0.1:8080", system => "upstream");
my $platform= AutoTest->new(host => "127.0.0.1:8080", system => "platform");
my $loginPwd = "12345678";

# 登录
$upstream->login("13939155835", $loginPwd);


# 添加公司
#&addCompany($upstream, "upstream");

#&getCompany($platform, "platform");

&getEmployees($upstream,"upstream");

sub getEmployees{
   my ($agent, $system) = @_;
    my $getCompany= $agent->get("--------获取公司信息-------", "/api/$system/employee/query?phoneNum=139",{jwt=>1});
    $agent->dump($getCompany)
}

sub getCompany{
    my ($agent, $system) = @_;
        my $getCompany= $agent->get("--------获取公司信息-------", "/api/$system/company/companies",{jwt=>1});
       $agent->dump($getCompany)
}


# 4.  修改手机号
#&update_Phone($upstream, "upstream", "13939155835");
sub update_Phone{

    my ($agent, $system, $newPhone) = @_;
    warn "-----------------------------------------------\n";
    warn "$system 修改手机号开始...\n";
    warn "-----------------------------------------------\n";
    my $u000 = $agent->get("0.触发服务器生成图形验证码", "/api/$system/captcha/get");
    my $u001 = $agent->get("1.获取图形验证码", "/api/$system/captcha/find");
    my $u002 = $agent->get("2 触发服务器发送短信验证码", "/api/$system/security/sendOldPhoneSms?imgCode=$u001", { jwt => 1 });
    my $u003 = $agent->get("3 获取短信验证码", "/api/$system/register/findSms?authCodeType=CHANGE_OLD_PHONE");
    my $u004 = $agent->get("4 验证老手机号验证码", "/api/$system/security/validateOldPhoneSms?smsCode=$u003", { jwt => 1 });
    # 给新手机号发送验证码
    my $u005 = $agent->get("5.触发服务器生成图形验证码", "/api/$system/captcha/get");
    my $u006 = $agent->get("6.获取图形验证码", "/api/$system/captcha/find");
    my $u008 = $agent->get("7 发送手机验证码到新手机号", "/api/$system/security/sendNewPhoneSms?imgCode=$u006&newPhone=$newPhone",
        { jwt => 1 });
    my $u009 = $agent->get("8 获取新手机号短信验证码", "/api/$system/register/findSms?authCodeType=CHANGE_NEW_PHONE",
        { jwt => 1 });
    my $u010 = $agent->get("9 修改手机号", "/api/$system/security/updatePhone?newPhone=$newPhone&smsCode=$u009",
        { jwt => 1 });
    #    &login_system($upstream, "upstream", $newPhone, $oldPwd);
}


sub update_Loginpassword{
    my ($agent, $system, $oldPwd, $newPwd) = @_;
    warn "-----------------------------------------------\n";
    warn "$system 修改登录密码开始...\n";
    warn "-----------------------------------------------\n";
    my $u000 = $agent->get("0.触发服务器生成图形验证码", "/api/$system/captcha/get");
    my $u001 = $agent->get("1.获取图形验证码", "/api/$system/captcha/find");
    my $u002 = $agent->get("2 触发服务器发送短信验证码", "/api/$system/security/sendPasswordSms?imgCode=$u001", { jwt => 1 });
    my $u003 = $agent->get("3 获取短信验证码", "/api/$system/register/findSms?authCodeType=UPDATE_LOGINPWD");
    my $u004 = $agent->get("4 验证原密码是否正确", "/api/$system/security/validateOldPwd?oldPassword=$oldPwd", { jwt => 1 });
    my $u005 = $agent->get("4 修改登录密码",
        "/api/$system/security/changePassword?oldPassword=$oldPwd&newPassword=$newPwd&smsCode=$u003", { jwt => 1 });
}

sub addCompany{
    my ($agent, $system) = @_;
    warn "-----------------------------------------------\n";
    warn "$system 添加公司开始...\n";
    warn "-----------------------------------------------\n";
    my $u001 = $agent->pos("1. 保存公司信息", "/api/$system/identify/saveCompany", {
            jwt  => 1,
            data => {
                "companyName"         => "aaaa",
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
                    "legalPersonName"             => "aaaa",
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
                    "authPersonName"             => "aaaa",
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

#    my $getCompany= $agent->get("--------获取公司信息-------", "/api/$system/identify/getCompanyInfo",{jwt=>1});
    my $getCompany= $agent->get("--------获取公司信息-------", "/api/platform/company/companyBasicDetail/5937d31c93c643694498e8d4",{jwt=>1});
    $agent->dump($getCompany)

}
