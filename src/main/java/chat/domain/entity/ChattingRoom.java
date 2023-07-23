package chat.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class ChattingRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String type;

  @ManyToOne
  @JoinColumn(name = "errand_id")
  private Errand errand;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String status;

  @OneToMany(mappedBy = "chattingRoom")
  @Column(nullable = false)
  private List<ChattingRoomMember> roomMemberList = new ArrayList<>();

  @Column(nullable = false)
  private LocalDateTime createAt;

  @PrePersist
  protected void onCreate() {
    this.createAt = LocalDateTime.now();
  }
}
