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
public class ChattingRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "errand_id")
  private Errand errand;

  @ManyToOne
  @JoinColumn(name = "members_id")
  private Members members;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false)
  private LocalDateTime createAt;

  @PrePersist
  protected void onCreate() {
    this.createAt = LocalDateTime.now();
  }

}
