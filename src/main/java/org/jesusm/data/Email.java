package org.jesusm.data;

public record Email(String to, String subject, String content) {
    @Override
    public String toString() {
        return  "To: " + to + "\n" +
                "Re: " + subject + "\n" +
                content;
    }
}
