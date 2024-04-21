package edu.java.scrapper.domain.jpa.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "link")
@Getter
@Setter
public class Link {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private OffsetDateTime updatedAt;
    private OffsetDateTime lastCheckedAt;
    private String lastCommitSha;
    private Long answersCount;

    @ManyToMany(mappedBy = "linkList")
    public List<Chat> chatList;

    public void addChat(Chat chat) {
        this.chatList.add(chat);
        chat.linkList.add(this);
    }

    public void deleteChat(Chat chat) {
        this.chatList.remove(chat);
        chat.linkList.remove(this);
    }

    @Override
    public String toString() {
        return "Link [id=" + id + ", url=" + url + ", updatedAt=" + updatedAt.toString()
        + ", lastCheckedAt=" + lastCheckedAt.toString() + ", lastCommitSha=" + lastCommitSha
        + ", answersCount=" + answersCount.toString() + ", chatList.size()="
        + chatList.size() + "]";
    }
}
