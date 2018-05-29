package com.dankook.jalgashoe.data.vo;

import java.util.ArrayList;
import java.util.List;

public class PathBundleVO {
    private PathPointVO point;
    private List<PathLineVO> lineList = new ArrayList<>();

    @Override
    public String toString() {
        return "PathBundleVO{" +
                "point=" + point +
                ", lineList=" + lineList +
                '}';
    }

    public PathPointVO getPoint() {
        return point;
    }

    public void setPoint(PathPointVO point) {
        this.point = point;
    }

    public List<PathLineVO> getLineList() {
        return lineList;
    }

    public void setLineList(List<PathLineVO> lineList) {
        this.lineList = lineList;
    }

    public void addLineList(PathLineVO line){
        lineList.add(line);
    }
}
