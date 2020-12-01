package com.example.petshare.donation;

public class DonationResponse {


    int user_id;

    int amount;

    String transaction_id;

    String donation_name;
    String donation_email;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getDonation_name() {
        return donation_name;
    }

    public void setDonation_name(String donation_name) {
        this.donation_name = donation_name;
    }

    public String getDonation_email() {
        return donation_email;
    }

    public void setDonation_email(String donation_email) {
        this.donation_email = donation_email;
    }
}
