package gettersetter;

public class DoWeNeedToSynchronize {
  private int day, month, year;
  private final Object rendezvous = new Object();

  public int getDay() {
    return day;
  }

  public void addDays(int days) {
    synchronized (this.rendezvous) {
      // all this stuff is transactionally delicate!
      int targetDay = this.day + days;
      // if targetDay > daysInThisMonth(month, year)
      // wrap around to a new day, month and perhaps even year
    }
  }

  public int getMonth() {
    // probably need this to be either a volatile variable
    // or in a sychronized block, otherwise we have zero
    // guarantee that we'll EVER see the updates!
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }
}
