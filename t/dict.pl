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

my $out;
$out = $upstream->get("获取所有字典",  "/api/dict/all");
$out = $upstream->get("获取所有字典",  "/api/dict/companyStatus");
$out = $upstream->get("获取所有字典",  "/api/dict/companyType");
$out = $upstream->get("获取所有字典",  "/api/dict/businessStatus");
$out = $upstream->get("获取xxx字典",  "/api/dict/financeStatus");
$out = $upstream->get("获取支付方式字典",  "/api/dict/payMode");
$out = $upstream->get("获取性别字典",  "/api/dict/sexType");
$out = $upstream->get("获取用户角色字典",  "/api/dict/userType");
$out = $upstream->get("获取运输方式字典",  "/api/dict/trafficMode");
$out = $upstream->get("获取应用状态字典",  "/api/dict/applicationStatus");

