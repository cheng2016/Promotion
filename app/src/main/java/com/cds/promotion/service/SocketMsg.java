package com.cds.promotion.service;

import com.cds.promotion.service.message.ContentMsg;
import com.cds.promotion.service.message.HeadMsg;
import com.cds.promotion.service.message.TailMsg;

public class SocketMsg {
    /**
     * head : {"token":"123445324234213412342134","msg_id":"123","msg_type":"2"}
     * content : {}
     * tail : {"time":"20150505140101"}
     */

    private HeadMsg head;
    private ContentMsg content;
    private TailMsg tail;

    public HeadMsg getHead() {
        return head;
    }

    public void setHead(HeadMsg head) {
        this.head = head;
    }

    public ContentMsg getContent() {
        return content;
    }

    public void setContent(ContentMsg content) {
        this.content = content;
    }

    public TailMsg getTail() {
        return tail;
    }

    public void setTail(TailMsg tail) {
        this.tail = tail;
    }
}
