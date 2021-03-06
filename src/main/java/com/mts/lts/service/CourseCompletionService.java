package com.mts.lts.service;

import com.mts.lts.dao.TopicRepository;
import com.mts.lts.domain.Course;
import com.mts.lts.domain.Module;
import com.mts.lts.domain.Topic;
import com.mts.lts.domain.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CourseCompletionService {

    private final UserListerService userListerService;
    private TopicRepository topicRepository;

    public CourseCompletionService(UserListerService userListerService,
                                   TopicRepository topicRepository) {
        this.userListerService = userListerService;
        this.topicRepository = topicRepository;
    }

    public void completeTopic(User user, Topic topic) {
        user.getCompletedTopics().add(topic);
        userListerService.save(user);
    }

    public Integer countTopicsForCourse(Course course) {
        return topicRepository.countTotalTopicsForCourse(course);
    }

    public Integer countTopicsCompletedByUser(User user, Course course) {
        int result = 0;
        for (Module m : course.getModules())
            for (Topic t : m.getTopics())
                if (t.getUsersWhoCompleted().contains(user))
                    result++;
        return result;
    }
}
