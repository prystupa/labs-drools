package com.prystupa;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: eprystupa
 * Date: 9/29/12
 * Time: 4:16 PM
 */
public class Application {
    private Date dateApplied;
    private boolean valid;

    public Application() {
        this.dateApplied = new Date();
        this.valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }
}
