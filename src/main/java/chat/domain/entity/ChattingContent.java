package chat.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChattingContent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "chattingRoom_id")
  private ChattingRoom chattingRoom;

  @Column(nullable = false)
  private Long senderId;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private String isRead;

  @Column(nullable = false)
  private LocalDateTime createAt;

  @PrePersist
  protected void onCreate() {
    this.createAt = LocalDateTime.now();
  }

}
