package com.dengjia.lib_share_asr.grammer;

public enum Place {

    DRAWING_ROOM(1, "客厅"),
    LIVING_ROOM(2, "房间"),
    BALCONY(3, "阳台"),
    TOILET(4, "厕所");

    private int number;
    private String place;

    Place(int number, String place) {
        this.number = number;
        this.place = place;
    }

    public int getNumber() {
        return number;
    }

    public String getPlace() {
        return place;
    }
}
