package me.ele.talaris.response;

import me.ele.talaris.model.Station;

public class StationInfo {
    private Station station;

    public StationInfo(Station station) {
        super();
        this.station = station;
    }

    public StationInfo() {
        super();
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

}
