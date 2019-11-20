package com.forgqi.resourcebaseserver.client.parse;

import com.forgqi.resourcebaseserver.client.JwkFeignClient;
import com.forgqi.resourcebaseserver.dto.CourseDTO;
import com.forgqi.resourcebaseserver.dto.StuInfoDTO;
import feign.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.forgqi.resourcebaseserver.client.parse.ParseUtil.OddEvenWeek;
import static com.forgqi.resourcebaseserver.client.parse.ParseUtil.getNum;

@Component
public class JwkParse {
    private final JwkFeignClient jwkFeignClient;


    public JwkParse(JwkFeignClient jwkFeignClient) {
        this.jwkFeignClient = jwkFeignClient;
    }

    public StuInfoDTO getStuInfo() throws IOException {
        Response response = jwkFeignClient.getStuInfo();

        Document document = ParseUtil.getDocument(response);

        Element element = document.getElementsByClass("form").first();
        StuInfoDTO stuInfoDTO = new StuInfoDTO();
        stuInfoDTO.setId(Long.valueOf(element.select("tr").get(0).select("td").get(0).text()));
        stuInfoDTO.setName(element.select("tr").get(0).select("td").get(1).text());
        stuInfoDTO.setCollege(element.select("tr").get(1).select("td").get(0).text());
        stuInfoDTO.setSubject(element.select("tr").get(1).select("td").get(1).text());
        stuInfoDTO.setEducation(element.select("tr").get(2).select("td").get(1).text());
        stuInfoDTO.setGrade(element.select("tr").get(3).select("td").get(0).text());
        stuInfoDTO.setClassNo(element.select("tr").get(3).select("td").get(1).text());
        stuInfoDTO.setIdCard(element.select("tr").get(4).select("td").get(1).text());
        return stuInfoDTO;
    }

    public List<CourseDTO> getCourse() throws IOException {
        Response currCourse = jwkFeignClient.getCurrCourse();
        Document document = ParseUtil.getDocument(currCourse);

        Elements lessons = document.select("table").get(3).getElementsByClass("infolist_common");

        StringBuilder error_course_name = new StringBuilder();

        List<CourseDTO> courseTimeList = new ArrayList<>();

        for (int i = 0; i < lessons.size(); i++) {

            String course_name;
            StringBuilder course_teacher;
            String course_credit;

            course_name = lessons.get(i).select("td").get(2).text();

            Elements course_teachers = lessons.get(i).select("td").get(3).select("a");

            if (course_teachers.size() == 0) {
                course_teacher = new StringBuilder("暂无授课教师");

            } else if (course_teachers.size() == 1) {

                course_teacher = new StringBuilder(course_teachers.get(0).text());

            } else {
                course_teacher = new StringBuilder(course_teachers.get(0).text());

                for (int t = 1; t < course_teachers.size(); t++) {
                    course_teacher.append(",").append(course_teachers.get(t).text());
                }
            }

            course_credit = lessons.get(i).select("td").get(4).text().trim();

            if (course_credit.length() == 0) course_credit = "暂无";

            Elements net = lessons.get(i).select("td").get(9).select("table");
            if (net.isEmpty()) {

                error_course_name.append("网络共享课：").append(course_name).append("，");

            } else {

                Elements week_num = net.select("tr");
                int week_n = week_num.size();

                //相同名字，不同上课时间，week_n
                for (Element element : week_num) {

                    CourseDTO courseDTO = new CourseDTO();

                    courseDTO.setCourseNum(i);
                    courseDTO.setCourseName(course_name);
                    courseDTO.setTeacher(course_teacher.toString());
                    courseDTO.setCourseCredit(course_credit);

                    courseDTO.setCourseColor(ParseUtil.COURSE_COLOR[i % ParseUtil.COURSE_COLOR.length]);

                    String course_weeks;//course_n这门课一周中第week_n次课，有哪些周上课，原始文件字符串

                    String course_time;
                    String course_room;

                    String course_day;//周几上课

                    String course_all;//周几上课

                    int span_num = 0;
                    int first_jie = 0;
                    int day = 0;


                    course_weeks = element.select("td").get(0).text();
                    if (course_weeks.length() == 0) course_weeks = "暂无";

                    course_day = element.select("td").get(1).text();
                    if (course_day.isEmpty()) course_day = "暂无";


                    course_time = element.select("td").get(2).text();

                    if (course_time.isEmpty()) {
                        course_time = "暂无节数";
                        error_course_name.append(course_name).append(course_time).append("，");

                    } else {
                        if (course_time.contains("中午")) {
                            Pattern p = Pattern.compile("[^0-9]");
                            Matcher m = p.matcher(course_time);
                            String result = m.replaceAll("");

                            if (result.length() == 1) {

                                span_num = 1;
                                first_jie = Integer.parseInt(result) + 4;
                            } else if (result.length() == 2) {
                                span_num = 2;
                                first_jie = Integer.parseInt(result.substring(0, 1)) + 4;

                            } else {
                                error_course_name.append(course_name).append(course_time).append("，");

                            }
                        } else if (course_time.contains("上午")) {
                            if (course_time.contains("-")) {
                                if (course_time.split("-").length == 2) {

                                    first_jie = Integer.parseInt(getNum(course_time.split("-")[0]));

                                    span_num = Integer.parseInt(getNum(course_time.split("-")[1])) - first_jie + 1;
                                }
                            } else if (getNum(course_time).length() == 2) {
                                first_jie = Integer.parseInt(getNum(course_time).substring(0, 1));
                                span_num = 2;

                            } else {
                                first_jie = Integer.parseInt(getNum(course_time).substring(0, 1));
                                int last_jie = Integer.parseInt(getNum(course_time).substring(getNum(course_time).length() - 1));
                                span_num = last_jie - first_jie + 1;
//                                        error_course_name.append(course_name).append(course_time).append("，");

                            }

                        } else if (course_time.contains("下午")) {
                            if (course_time.contains("-")) {
                                if (course_time.split("-").length == 2) {

                                    first_jie = Integer.parseInt(getNum(course_time.split("-")[0]));

                                    span_num = Integer.parseInt(getNum(course_time.split("-")[1])) - first_jie + 1;
                                }
                            } else if (getNum(course_time).length() == 2) {
                                first_jie = Integer.parseInt(getNum(course_time).substring(0, 1));
                                span_num = 2;

                            } else {
                                first_jie = Integer.parseInt(getNum(course_time).substring(0, 1));
                                int last_jie = Integer.parseInt(getNum(course_time).substring(getNum(course_time).length() - 1));
                                span_num = last_jie - first_jie + 1;
//                                        error_course_name.append(course_name).append(course_time).append("，");

                            }
                            first_jie = first_jie + 2;

                        } else if (course_time.contains("晚")) {
                            if (course_time.contains("-") && course_time.split("-").length == 2) {

                                first_jie = Integer.parseInt(getNum(course_time.split("-")[0]));

                                span_num = Integer.parseInt(getNum(course_time.split("-")[1])) - first_jie + 1;
                            } else {
                                error_course_name.append(course_name).append(course_time).append("，");

                            }

                            first_jie = first_jie + 2;

                        } else {
                            if (course_time.contains("-") && course_time.split("-").length == 2) {

                                first_jie = Integer.parseInt(getNum(course_time.split("-")[0]));
                                span_num = Integer.parseInt(getNum(course_time.split("-")[1])) - first_jie + 1;

                            } else {

                                first_jie = Integer.parseInt(getNum(course_time));
                                span_num = 1;

                            }
                            if (first_jie >= 5) first_jie = first_jie + 2;
                        }
                        //                        }
                        course_room = element.select("td").get(3).text();
                        if (course_room.isEmpty()) {
                            course_room = "暂无地点";
                        }
                        day = ParseUtil.paresZh(course_day.charAt(course_day.length() - 1));
                        if (course_name.length() > 7) {
                            course_name = course_name.substring(0, 7) + "…";
                        }

                        if (course_teacher.length() > 7) {
                            course_teacher = new StringBuilder(course_teacher.substring(0, 7) + "…");
                        }

                        course_all = course_name + "\n" + course_credit + "\n" + course_teacher + "\n@" + course_room;

                        courseDTO.setRoom(course_room);
                        courseDTO.setWeek(course_weeks);
//                        courseDTO.setCourseTime(course_time);

                        courseDTO.setDay(day);
                        courseDTO.setSpan(span_num);
                        courseDTO.setCourseStartTime(first_jie);

                        courseDTO.setTime(course_time);

                        courseDTO.setWeekList(OddEvenWeek(course_weeks));

                        courseTimeList.add(courseDTO);

                    }

                }
            }
        }
        return courseTimeList;
    }


}
