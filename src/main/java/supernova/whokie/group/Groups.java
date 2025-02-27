package supernova.whokie.group;

import jakarta.persistence.*;
import lombok.*;
import supernova.whokie.global.entity.BaseTimeEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "`GROUPS`")  // mysql 예약어로 인해 백틱으로 감쌈
public class Groups extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String groupImageUrl;

    public void modify(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    public void modifyImageUrl(String imageUrl) {
        this.groupImageUrl = imageUrl;
    }
}
