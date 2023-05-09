package com.sophra.test_parsing;

public class card_item {
    private String title;
    private String link;
    private String contents;
    private String img_url;
    private String web_url;
    private String img_url_test;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public card_item(String title, String link, String contents, String img_url, String web_url) {
        this.title = title;
        this.link = link;
        this.contents = contents;
        this.img_url = img_url;
        this.web_url = web_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }
}
