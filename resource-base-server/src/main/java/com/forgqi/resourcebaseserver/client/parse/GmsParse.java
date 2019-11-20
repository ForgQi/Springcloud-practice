package com.forgqi.resourcebaseserver.client.parse;

import com.forgqi.resourcebaseserver.client.GmsFeignClient;
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

import static com.forgqi.resourcebaseserver.client.parse.ParseUtil.OddEvenWeek;
import static com.forgqi.resourcebaseserver.client.parse.ParseUtil.getNum;

@Component
public class GmsParse {
    private final GmsFeignClient gmsFeignClient;

    public GmsParse(GmsFeignClient gmsFeignClient) {
        this.gmsFeignClient = gmsFeignClient;
    }

    public StuInfoDTO getStuInfo() throws IOException {
        Response response = gmsFeignClient.getStuInfo();
        Document document = ParseUtil.getDocument(response);

        Element element = document.getElementsByClass("xSectionForm").first();
        Element child1 = element.child(0);
        Element child2 = element.child(1);
        StuInfoDTO stuInfoDTO = new StuInfoDTO();
        stuInfoDTO.setId(Long.valueOf(child1.select("tr").get(0).select("td").get(0).text()));
        stuInfoDTO.setName(child1.select("tr").get(1).select("td").get(0).text());
        stuInfoDTO.setCollege(child2.select("td").get(11).text());
        stuInfoDTO.setSubject(child2.select("td").get(13).text());
        stuInfoDTO.setEducation(child2.select("td").get(9).text());
        stuInfoDTO.setGrade(child2.select("td").get(4).text());
        stuInfoDTO.setClassNo(child2.select("td").get(17).text());
        stuInfoDTO.setIdCard(child1.select("tr").get(6).select("td").get(0).text());
        return stuInfoDTO;
    }

    public List<CourseDTO> getCourse() throws IOException {
        Response currCourse = gmsFeignClient.getCurrCourse();
        Document document = ParseUtil.getDocument(currCourse);


        Elements lessons = document.getElementsByClass("datalist").first().select("tbody").first().select("tr");

        StringBuilder error_course_name = new StringBuilder();

        List<CourseDTO> courseTimeList = new ArrayList<>();

        for (int i = 0; i < lessons.size(); i++) {

            String course_name;
            StringBuilder course_teacher;
            String course_credit;

            course_name = lessons.get(i).select("td").get(1).text();

            Elements course_teachers = lessons.get(i).select("td").get(4).select("a");

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


            course_credit = lessons.get(i).select("td").get(3).text().trim();

            if (course_credit.length() == 0) course_credit = "暂无";

            String net = lessons.get(i).select("td").get(5).toString();
            if (net.isEmpty()) {

                error_course_name.append("网络共享课：").append(course_name).append("，");

            } else {

                String[] week_num = net.split("<br>");
                int week_n = week_num.length - 1;

                //相同名字，不同上课时间，week_n
                for (int k = 0; k < week_n; k++) {

                    CourseDTO courseDTO = new CourseDTO();

                    courseDTO.setCourseNum(i);
                    courseDTO.setCourseName(course_name);
                    courseDTO.setTeacher(course_teacher.toString());
                    courseDTO.setCourseCredit(course_credit);
                    courseDTO.setCourseColor(ParseUtil.COURSE_COLOR[i % ParseUtil.COURSE_COLOR.length]);

                    String course_weeks = null;//course_n这门课一周中第week_n次课，有哪些周上课，原始文件字符串

                    String course_time;
                    String course_room = null;

                    String course_day = null;//周几上课

                    String course_all;//周几上课

                    int span_num = 0;
                    int first_jie = 0;
                    int day = 0;

                    try {

                        String[] s = week_num[k].split("-");

                        try {
                            course_room = s[4];
                        } catch (Exception e) {
                            course_room = "暂无地点";
                            e.printStackTrace();
                        }

                        try {
                            course_weeks = getNum(s[0]) + "-" + getNum(s[1]);
                        } catch (Exception e) {
                            course_weeks = "暂无时间";
                            e.printStackTrace();
                        }

                        try {
                            course_day = s[1].replaceAll("[^(\\u4e00-\\u9fa5)]", "");

                        } catch (Exception e) {
                            course_day = "暂无时间";
                            e.printStackTrace();
                        }



                        course_time = s[0] + "-" + s[1] + "，" + s[2] + "-" + s[3];
                        course_time = course_time.replaceAll("<td>", "");
                        first_jie = Integer.parseInt(getNum(s[2]));
                        int sec_jie = Integer.parseInt(getNum(s[3]));

                        if (first_jie >= 5) {
                            first_jie = first_jie + 2;
                            sec_jie = sec_jie + 2;
                        }

                        span_num = sec_jie - first_jie + 1;

                    } catch (Exception e) {
                        course_time = "周数";
                        e.printStackTrace();
                    }
                    day = ParseUtil.paresZh(course_day.charAt(course_day.length() - 1));


                    if (course_name.length() > 7) {
                        course_name = course_name.substring(0, 7) + "…";
                    }

                    if (course_teacher.length() > 7) {
                        course_teacher = new StringBuilder(course_teacher.substring(0, 7) + "…");
                    }

                    course_all = course_name + "\n" + course_credit + "\n" + course_teacher + "\n@" + course_room;

//                    courseDTO.setRoom(course_room);
//                    courseDTO.setWeekList(course_weeks);
//                    courseDTO.setCourseTime(course_time);
//                    courseDTO.setUser(student);
//
//                    courseDTO.setDay(day);
//                    courseDTO.setSpanNum(span_num);
//                    courseDTO.setJieci(first_jie);
//
//                    courseDTO.setCourseAll(course_all);
//
//                    courseDTO.setCourseWeekList(OddEvenWeek(course_weeks));
//
//                    courseTimeList.add(courseDTO);

//                    弱智
                    courseDTO.setRoom(course_room);
                    courseDTO.setWeek(course_weeks);

                    courseDTO.setDay(day);
                    courseDTO.setSpan(span_num);
                    courseDTO.setCourseStartTime(first_jie);

                    courseDTO.setTime(course_time);

                    courseDTO.setWeekList(OddEvenWeek(course_weeks));

                    courseTimeList.add(courseDTO);

                }
            }
        }
        return courseTimeList;
    }
}

