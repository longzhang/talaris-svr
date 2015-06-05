package me.ele.talaris.response;

import java.util.List;

import me.ele.talaris.model.User;

/**
 * 配送员的业绩信息
 * 
 * @author zhengwen
 *
 */
public class AchievementsBelongToUser {
    private List<Achievement> achievements;

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public AchievementsBelongToUser(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    public AchievementsBelongToUser() {
        super();
    }

}
