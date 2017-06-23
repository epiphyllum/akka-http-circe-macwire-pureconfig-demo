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

#$upstream->clear();

my $phone = "13939155835";
my $loginPwd = "12345678";
my $employePhone = "13782705657";

# 登录
#$upstream->register($phone, "zxy1", "zxy1", $loginPwd);
$upstream->login($employePhone, "123");
$upstream->login($phone, $loginPwd);

#&addEmployees($upstream, "upstream");
#&disabledAccount($upstream, "upstream", "5938b8a393c6432db3e06f21");


sub addEmployees{
    my ($agent, $system) = @_;
    my $getEmployees = $agent->get("获取所有员工", "/api/$system/employee/query", { jwt => 1 });
    my $employeeId = $getEmployees->{data}->{items}->[0]->{_id};
    $agent->dump($getEmployees);


    #    新员工手机号
    my $addEmployee = $agent->pos("--------添加员工-------", "/api/$system/employee/save",
        {
            jwt  => 1,
            data => {
                "loginName" => "aaaa",
                "userName"  => "aaa",
                "phone"     => $employePhone,
                "dept"      => "财务部",
                "userType"  => 1,
            }
        });

    #    新员工登录
    $agent->login($employePhone, $loginPwd);

}

sub disabledAccount{

    my ($agent, $system, $id) = @_;
    my $disabledAccount = $agent->get("1. 禁用新员工账号", "/api/$system/employee/disabledAccount/$id", { jwt => 1 });
    my $resetPwd = $agent->get("2. 重置密码", "/api/$system/employee/resetPwd/$id", { jwt => 1 });

    my $enabledAccount = $agent->get("启用新员工账号", "/api/$system/employee/enabledAccount/$id", { jwt => 1 });

    $agent->login($employePhone, "123");

}



sub resetPassword{
    my ($agent, $system) = @_;
    my $getCompany = $agent->get("--------获取公司信息-------", "/api/$system/employee/query", { jwt => 1 });
    $agent->dump($getCompany)
}

sub getCompany{
    my ($agent, $system) = @_;
    my $getCompany = $agent->get("--------获取公司信息-------", "/api/$system/company/companies", { jwt => 1 });
    $agent->dump($getCompany)
}



