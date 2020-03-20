package sr.unasat.financeapp.entities;

import java.util.Date;

/**
 * Created by Benito on 09-Mar-18.
 */

public class Transaction {
    public int id;
    public String type;
    public String title;
    public double amount;
    public long date;

    public Transaction(int id, String type, String title, double amount, long date) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.amount = amount;
        this.date = date;
    }

    public Transaction(String type, String title, double amount) {
        this.type = type;
        this.title = title;
        this.amount = amount;
    }

    public Transaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", amount='" + amount + '\'' +
                ", date=" + date +
                '}';
    }
}