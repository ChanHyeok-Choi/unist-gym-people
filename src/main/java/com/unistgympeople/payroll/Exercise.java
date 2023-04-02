package com.unistgympeople.payroll;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Exercise {
    private @Id @GeneratedValue Long id;
    private int month;
    private int day;
    private int Weekday;
    private int hour;
    private int num;

    Exercise() {}
    Exercise( int a, int b, int c, int d, int e)
    {
        this.month = a;
        this.day = b;
        this.Weekday = c;
        this.hour = d;
        this.num = e;
    }
    public Long getId() {
        return this.id;
    }
    public int getMonth() { return this.month; }
    public int getDay() { return this.day; }
    public int getWeekday() { return this.Weekday; }
    public int getHour() {return this.hour;}
    public int getNum() {return this.num;}

    public void setId(Long id) {
        this.id = id;
    }
    public void setMonth(int month) { this.month = month; }
    public void setDay(int day) { this.day = day; }
    public void setWeekday(int Weekday) { this.Weekday=Weekday; }
    public void setHour(int hour) { this.hour = hour;}
    public void setNum(int num) {this.num = num;}

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.month, this.day, this.Weekday, this.hour, this.num);
    }
    @Override
    public String toString() {
        return "{" + "id=" + this.id + ", date='" + this.month +"/" + this.day + '\'' + ", time='" + this.hour + '\'' + " num=" + this.num+'\''+'}';
    }
}
