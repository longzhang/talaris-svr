package me.ele.talaris.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Context {
    private User user;
    private List<UserStationRole> userStationRoles;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserStationRole> getUserStationRoles() {
        return userStationRoles;
    }

    public void setUserStationRoles(List<UserStationRole> userStationRoles) {
        this.userStationRoles = userStationRoles;
    }

    public Context(User user, List<UserStationRole> userStationRoles) {
        this.user = user;
        this.userStationRoles = userStationRoles;
    }

    public Set<Integer> getStationID() {
        Set<Integer> stationIds = new HashSet<Integer>();
        for (UserStationRole userStationRole : userStationRoles) {
            stationIds.add(userStationRole.getStation_id());
        }
        return stationIds;
    }

    public Context() {
        super();
    }

}
