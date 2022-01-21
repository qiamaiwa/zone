package com.example.zone.controller;

import com.example.zone.service.AlphaService;
import com.example.zone.utils.ZoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping(path = "/alpha")
public class alphaController {

    @Autowired
    private AlphaService alphaService;


    @RequestMapping(path = "/hello")
    @ResponseBody
    public String sayHello(){ return "hello!";}


    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try (
                PrintWriter writer = response.getWriter();
        ) {
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  GET请求
    // students?current=1&limit=20
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

//
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

//     POST请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 响应HTML数据
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "张三");
        mav.addObject("age", 30);
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "北京大学");
        model.addAttribute("age", 80);
        return "/demo/view";
    }

//     响应JSON数据(异步请求)
//     Java对象 -> JSON字符串 -> JS对象

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "张三");
        emp.put("age", 23);
        emp.put("salary", 8000.00);
        return emp;
    }
    //会把map转成一个json字符串 发送给浏览器

//    @RequestMapping(path = "/emps", method = RequestMethod.GET)
//    @ResponseBody
//    public List<Map<String, Object>> getEmps() {
//        List<Map<String, Object>> list = new ArrayList<>();
//
//        Map<String, Object> emp = new HashMap<>();
//        emp.put("name", "张三");
//        emp.put("age", 23);
//        emp.put("salary", 8000.00);
//        list.add(emp);
//
//        emp = new HashMap<>();
//        emp.put("name", "李四");
//        emp.put("age", 24);
//        emp.put("salary", 9000.00);
//        list.add(emp);
//
//        emp = new HashMap<>();
//        emp.put("name", "王五");
//        emp.put("age", 25);
//        emp.put("salary", 10000.00);
//        list.add(emp);
//
//        return list;
//    }

//    会话管理
//    设置cookie值
    @RequestMapping(path = "/coookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse httpServletResponse){
        Cookie cookie=new Cookie("code",ZoneUtil.generateUUID());
        cookie.setMaxAge(60*10);
        cookie.setPath("/zone/alpha");
        httpServletResponse.addCookie(cookie);
        return "set cookie";
    }

//    从request得到cookie值 8944270cf2b343718dbd10061bd98601
    @RequestMapping(path = "/coookie/get",method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String  code){
        System.out.println(code);
        return "get cookie";
    }


    //session在服务器端生成在服务器端保存，并把sessionID放在cookie中，放在向应里传给客户端。
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("id",1);
        session.setAttribute("name","llq");
        return "set session";
    }


    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

    @RequestMapping(path = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public  String ajax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return ZoneUtil.getJSONString(0,"操作成功！");

    }



}
