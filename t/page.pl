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


my $upstream = AutoTest->new(system => "upstream", host => "127.0.0.1:8080");

$upstream->get("定制的page指令测试", "/api/mock/pageTest?pageNum=10&pageSize=100&a=88&b=99");
eval { $upstream->get("fail1指令", "/api/mock/failTest1"); }; if($@) { warn "$@"; }
eval { $upstream->get("fail2指令", "/api/mock/failTest2"); }; if($@) { warn "$@"; }
eval { $upstream->get("fail3指令", "/api/mock/failTest3"); }; if($@) { warn "$@"; }


