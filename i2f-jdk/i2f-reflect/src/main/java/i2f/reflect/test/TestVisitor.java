package i2f.reflect.test;

import i2f.reflect.vistor.Visitor;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/1 8:54
 * @desc context={
 * name: '超级管理员',
 * perm: [{
 * perms: ['add','delete'],
 * get(index){
 * return this.perms[index]
 * }
 * }],
 * category:{
 * charIdx: 0
 * },
 * requires:{
 * permIdx: 1
 * }
 * }
 * <p>
 * root={
 * user:{
 * roles: [{
 * name: '超级管理员',
 * key: 'root',
 * perms: ['add','delete','edit'],
 * getKeys(name,perm){
 * if(name!=this.name){
 * return ['view']
 * }
 * if(!this.perms.contains(perm)){
 * return ['view']
 * }
 * return [perm,'view']
 * }
 * },{
 * name: '管理员',
 * key: 'admin',
 * perms: ['add','edit']
 * }]
 * }
 * }
 * <p>
 * expression= user.roles[0].@getKeys(#{name},#{age}[0].@get(#{current.key}))[0].@charAt(#{category.charIdx})
 * ret= 'd'
 */


public class TestVisitor {

    public static void main(String[] args) {
        String expression = "user.roles[0].@getKeys(#{name},#{perm}[0].@get(#{requires.permIdx}))[0].@charAt(#{category.charIdx})";
//        expression="user.roles[0].@getKeys(#{name},#{perm}[0].@get(#{requires.permIdx}))[0].@isEmpty()";
//        expression="@String.valueOf($23.565)";
        expression = "@Math.PI";
        Map<String, Object> rootObj = new HashMap<>();

        List<TestRole> userRoles = new ArrayList<>();
        TestRole role1 = new TestRole();
        role1.setName("超级管理员");
        role1.setKey("root");
        role1.setPerms(Arrays.asList("add", "delete", "edit"));
        userRoles.add(role1);
        TestRole role2 = new TestRole();
        role2.setName("管理员");
        role2.setKey("admin");
        role2.setPerms(Arrays.asList("add", "edit"));
        userRoles.add(role2);

        TestUser user = new TestUser();
        user.setRoles(userRoles);
        rootObj.put("user", user);


        TestContext paramObj = new TestContext();
        paramObj.setName("超级管理员");
        TestContextPerm contextPerm = new TestContextPerm();
        contextPerm.setPerms(Arrays.asList("add", "delete"));
        paramObj.setPerm(Arrays.asList(contextPerm));
        TestContextCategory contextCategory = new TestContextCategory();
        contextCategory.setCharIdx(0);
        paramObj.setCategory(contextCategory);
        TestContextRequires contextRequires = new TestContextRequires();
        contextRequires.setPermIdx(1);
        paramObj.setRequires(contextRequires);


        Visitor ret = Visitor.visit(expression, rootObj, paramObj);

        System.out.println(ret.getClass().getName());
        System.out.println(ret.get());

        ret.set(666);
        System.out.println(ret.get());
    }


}
