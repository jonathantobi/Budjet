package sr.unasat.financeapp.entities;

/**
 * Created by Benito on 11-Mar-18.
 */

public class Goal {
    public int id;
    public double amount;
    public long date;

    public Goal(int id, double amount, long date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }

    public Goal(double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "Goal{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}