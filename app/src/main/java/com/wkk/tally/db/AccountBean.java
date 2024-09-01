package com.wkk.tally.db;

public class AccountBean {
    // 数据库中每条数据的记录类
    int id;
    String typename; // 类型 吃饭烟酒茶之类的
    int sImageId;
    String beiZhu;
    float money;
    String time;
    int year = 0;
    int month = 0;
    int day = 0;
    int kind; // 类型 收入 1 支出 0

    public AccountBean() {
    }

    public AccountBean(int kind, int day, int month, int year, String time, float money, String beiZhu, int sImageId, String typename, int id) {
        this.kind = kind;
        this.day = day;
        this.month = month;
        this.year = year;
        this.time = time;
        this.money = money;
        this.beiZhu = beiZhu;
        this.sImageId = sImageId;
        this.typename = typename;
        this.id = id;
    }
    public AccountBean(int id, String typename, int sImageId, String beizhu, float money, String time, int year, int month, int day, int kind) {
        this.id = id;
        this.typename = typename;
        this.sImageId = sImageId;
        this.beiZhu = beizhu;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getBeiZhu() {
        return beiZhu;
    }

    public void setBeiZhu(String beiZhu) {
        this.beiZhu = beiZhu;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "AccountBean{" +
                "id=" + id +
                ", typename='" + typename + '\'' +
                ", sImageId=" + sImageId +
                ", beiZhu='" + beiZhu + '\'' +
                ", money=" + money +
                ", time='" + time + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", kind=" + kind +
                '}';
    }
}
