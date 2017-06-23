#!/usr/bin/perl
use strict;

use lib "t/lib";
use AutoTest;
use JSON;
use utf8 ':all';
use Encode;
use Data::Dumper;
use Getopt::Long;
use UUID qw/uuid/;

my $upstream = AutoTest->new(host => "127.0.0.1:8080", system => "upstream");
$upstream->clear();

# 1. 注册用户
my $loginPwd = "12345678";
$upstream->register("13939155835", "zxy1", "zxy1", $loginPwd);

# 2. 用户登录
$upstream->login("13939155835", $loginPwd);

# 3. 获取上传policy
my $resp = $upstream->get("获取policy", "/api/asset/policy", { jwt => 1});

# 4. 上传文件
my $uuid = uuid();
my $uploadCmd =<<"EOF";
curl -XPOST
  -F "key=$resp->{dirName}/$uuid.jpg"
  -F "policy=$resp->{policy}"
  -F "callback=$resp->{callback}"
  -F "OSSAccessKeyId=$resp->{OSSAccessKeyId}"
  -F "signature=$resp->{signature}"
  -F "x:realname=myfilename.jpg"
  -F file="\@README.md"
  "$resp->{host}"
EOF
$uploadCmd =~ s/\n//mg;
warn "$uploadCmd\n";
system($uploadCmd);

# 5. 下载文件名
my $file = uuid();
my $downloadCmd =<<"EOF";
curl -s -o /tmp/$file "$resp->{host}/$resp->{dirName}/$uuid.jpg"
EOF
warn "$downloadCmd\n";
system($downloadCmd);
system("diff /tmp/$file README.md");




