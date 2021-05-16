package com.example.reservarmesas;

public class Body {
    private String head,body;
    private boolean expandable;

    public Body(String head,String body){
        this.head=head;
        this.body=body;
        this.expandable=false;
    }

    public boolean isExpandable(){
        return expandable;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    @Override
    public String toString(){
        return "Body{" +
                "head='" + head + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
