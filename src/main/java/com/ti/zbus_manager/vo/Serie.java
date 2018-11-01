package com.ti.zbus_manager.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Serie {
    @Data
    class Marker {
        private String symbol;
        public Marker(String symbol){
            this.symbol = symbol;
        }
    }

    private String name;
    private Marker marker = new Marker("diamond");
    private ArrayList<Integer> data;
}
