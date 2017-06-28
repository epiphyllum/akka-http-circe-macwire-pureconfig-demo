#!/usr/bin/perl
use strict;
use JSON;
use utf8 ':all';
use Encode;
use Data::Dumper;
use Getopt::Long;
use lib "t/lib";
use AutoTest;

# 0. 清理数据库
AutoTest->clear();

# 1. 创建浏览器
my $upstream = AutoTest->new(host => "127.0.0.1:8080", system => "upstream");
my $bridge   = AutoTest->new(host => "127.0.0.1:8080", system => "bridge"  );
my $capital  = AutoTest->new(host => "127.0.0.1:8080", system => "capital" );
my $platform = AutoTest->new(host => "127.0.0.1:8080", system => "platform");

# 2. 为各平台注册用户
$upstream->register("1300000000", "upstream", "upstream", "12345678");
$bridge->register(  "1300000001", "bridge",   "bridge",   "12345678");
$capital->register( "1300000002", "capital",  "capital",  "12345678");
$platform->register("1300000003", "platform", "platform", "12345678");

# 3. 各平台用户登录
$upstream->login("1300000000", "12345678");
$bridge->login(  "1300000001", "12345678");
$capital->login( "1300000002", "12345678");
$platform->login("1300000003", "12345678");

# 4. 各平台用户添加公司
$upstream->add_company("上游公司1");
$bridge->add_company("核心企业1");
$capital->add_company("资金方1");

# 5 审核公司通过
# $platform->get("审核公司认证", "/api/platform/company/$id/auditSuccess/审核意见", { jwt => 1})
# $platform->get("审核公司认证", "/api/platform/company/$id/auditSuccess/审核意见", { jwt => 1})
# $platform->get("审核公司认证", "/api/platform/company/$id/auditSuccess/审核意见", { jwt => 1})

