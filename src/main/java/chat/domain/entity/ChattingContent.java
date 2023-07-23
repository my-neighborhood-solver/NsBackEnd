package chat.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

  @OneToMany(mappedBy = "chattingContent", cascade = CascadeType.ALL) // 다수의 사용자와의 관계
  private List<ChattingContentReadStatus> chattingContentReadStatuses = new ArrayList<>();

  @Column(nullable = false)
  private String isRead;

  @Column(nullable = false)
  private LocalDateTime createAt;

  @PrePersist
  protected void onCreate() {
    this.createAt = LocalDateTime.now();
  }
}
