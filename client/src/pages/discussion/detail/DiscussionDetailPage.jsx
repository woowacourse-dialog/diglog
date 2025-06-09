import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { FaHeart, FaRegHeart, FaBookmark, FaRegBookmark } from 'react-icons/fa';
import Header from '../../../components/Header/Header';
import './DiscussionDetailPage.css';

const getDiscussionStatus = (startDateTime, endDateTime) => {
  const now = new Date();
  const start = new Date(startDateTime);
  const end = new Date(endDateTime);

  if (now < start) {
    return { status: 'upcoming', label: '시작 전' };
  } else if (now > end) {
    return { status: 'ended', label: '마감됨' };
  } else {
    return { status: 'ongoing', label: '진행중' };
  }
};

const DiscussionDetailPage = () => {
  const { id } = useParams();
  const [discussion, setDiscussion] = useState(null);
  const [loading, setLoading] = useState(true);
  const [joining, setJoining] = useState(false);
  const [isLiked, setIsLiked] = useState(false);
  const [isBookmarked, setIsBookmarked] = useState(false);
  const [likeCount, setLikeCount] = useState(0);

  useEffect(() => {
    // TODO: API 연동 후 실제 데이터로 교체
    const fetchDiscussion = async () => {
      try {
        // 임시 데이터
        const mockDiscussion = {
          id,
          title: "Spring Boot vs Spring MVC - 현대적인 Java 웹 개발 비교",
          track: "backend",
          trackName: "백엔드",
          content: `# Spring Boot vs Spring MVC 비교 및 모던 Java 웹 개발 동향

## 소개

최근 Java 웹 개발 생태계에서는 Spring Boot의 채택이 늘어나고 있습니다. 전통적인 Spring MVC와 비교하여 어떤 장단점이 있는지, 그리고 실제 프로젝트에서 어떻게 선택해야 할지 논의해보고자 합니다.

## 1. Spring MVC

### 특징
- XML 기반의 상세한 설정 가능
- 세밀한 커스터마이징
- 전통적인 웹 애플리케이션 구조
- WAS(Tomcat, JBoss 등) 필요

### 설정 예시
\`\`\`xml
<!-- web.xml -->
<web-app>
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/spring-mvc-config.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
</web-app>
\`\`\`

### Controller 예시
\`\`\`java
@Controller
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
}
\`\`\`

## 2. Spring Boot

### 특징
- 자동 설정 (AutoConfiguration)
- 내장 서버 지원
- 스타터 종속성
- 프로덕션 준비 기능

### 설정 예시
\`\`\`java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
\`\`\`

### REST API 예시
\`\`\`java
@RestController
@RequestMapping("/api/v1")
public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping("/products")
    public List<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.findAll(PageRequest.of(page, size));
    }
    
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDTO dto) {
        Product product = productService.create(dto);
        return ResponseEntity
            .created(URI.create("/api/v1/products/" + product.getId()))
            .body(product);
    }
}
\`\`\`

## 3. 최신 트렌드와 기술

### 3.1 리액티브 프로그래밍
Spring WebFlux를 활용한 비동기 프로그래밍:

\`\`\`java
@RestController
public class ReactiveController {
    
    @GetMapping("/events")
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(sequence -> ServerSentEvent.<String>builder()
                .id(String.valueOf(sequence))
                .event("periodic-event")
                .data("SSE Event #" + sequence)
                .build());
    }
}
\`\`\`

### 3.2 클라우드 네이티브
Spring Cloud와의 통합:

\`\`\`yaml
# application.yml
spring:
  cloud:
    config:
      uri: http://config-server:8888
    discovery:
      enabled: true
  application:
    name: my-service
\`\`\`

## 4. 성능 최적화

### 4.1 JPA 최적화
\`\`\`java
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}
\`\`\`

### 4.2 캐싱 전략
\`\`\`java
@Service
@CacheConfig(cacheNames = {"products"})
public class ProductService {
    
    @Cacheable(key = "#id")
    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }
    
    @CacheEvict(allEntries = true)
    public void refreshCache() {
        // 캐시 초기화 로직
    }
}
\`\`\`

## 토론 주제

1. Spring Boot의 자동 설정(Auto-configuration)이 개발 생산성과 유지보수성에 미치는 영향은?
2. 마이크로서비스 아키텍처에서 Spring Cloud vs Kubernetes 선택 기준은?
3. 대규모 트래픽 처리를 위한 Spring 기반 아키텍처 설계 전략은?
4. Spring WebFlux는 언제 사용하는 것이 적절한가?
5. JPA vs MyBatis - 어떤 상황에서 어떤 것을 선택해야 하는가?

## 실제 사례 연구

1. 대형 커머스 플랫폼의 Spring Boot 마이그레이션 사례
2. 금융권 Spring Security 적용 사례
3. 소셜 미디어 플랫폼의 실시간 처리 아키텍처

이러한 주제들을 바탕으로 각자의 경험과 의견을 나누어 보면 좋겠습니다.`,
          location: "온라인 줌 미팅",
          participantCount: 4,
          maxParticipants: 6,
          startDateTime: "2024-03-20T14:00:00Z",
          endDateTime: "2024-03-20T16:00:00Z",
          createdAt: "2024-03-15T09:00:00Z",
          likeCount: 42,
          creator: {
            id: "user123",
            name: "김개발",
            profileImage: "https://via.placeholder.com/40"
          },
          participants: [
            { id: "user123", name: "김개발" },
            { id: "user456", name: "이스프링" },
            { id: "user789", name: "박자바" }
          ]
        };

        setDiscussion(mockDiscussion);
        setLikeCount(mockDiscussion.likeCount);
        setLoading(false);
      } catch (error) {
        console.error('Failed to fetch discussion:', error);
        setLoading(false);
      }
    };

    fetchDiscussion();
  }, [id]);

  const handleJoin = async () => {
    setJoining(true);
    try {
      // TODO: API 연동
      await new Promise(resolve => setTimeout(resolve, 1000)); // 임시 딜레이
      alert('토론 참여가 완료되었습니다!');
      // TODO: 참여자 목록 업데이트
    } catch (error) {
      console.error('Failed to join discussion:', error);
      alert('토론 참여에 실패했습니다. 다시 시도해주세요.');
    }
    setJoining(false);
  };

  const handleLike = async () => {
    try {
      // TODO: API 연동
      setIsLiked(!isLiked);
      setLikeCount(prevCount => isLiked ? prevCount - 1 : prevCount + 1);
    } catch (error) {
      console.error('Failed to update like:', error);
    }
  };

  const handleBookmark = async () => {
    try {
      // TODO: API 연동
      setIsBookmarked(!isBookmarked);
    } catch (error) {
      console.error('Failed to update bookmark:', error);
    }
  };

  if (loading) {
    return <div className="discussion-detail-loading">Loading...</div>;
  }

  if (!discussion) {
    return <div className="discussion-detail-error">토론을 찾을 수 없습니다.</div>;
  }

  const { status, label } = getDiscussionStatus(discussion.startDateTime, discussion.endDateTime);

  const formatDateTime = (dateTimeStr) => {
    const date = new Date(dateTimeStr);
    return new Intl.DateTimeFormat('ko-KR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    }).format(date);
  };

  return (
    <div className="discussion-detail-page">
      <Header />
      <div className="discussion-detail-container">
        <div className="discussion-detail-wrapper">
          <div className="discussion-detail-header">
            <div className="discussion-header-top">
              <div className="discussion-track">{discussion.trackName}</div>
              <div className={`discussion-status ${status}`}>{label}</div>
            </div>
            <div className="discussion-title-row">
              <h1>{discussion.title}</h1>
              <div className="discussion-actions">
                <button 
                  className={`action-button ${isLiked ? 'liked' : ''}`}
                  onClick={handleLike}
                >
                  {isLiked ? <FaHeart /> : <FaRegHeart />}
                  <span>{likeCount}</span>
                </button>
                <button 
                  className={`action-button ${isBookmarked ? 'bookmarked' : ''}`}
                  onClick={handleBookmark}
                >
                  {isBookmarked ? <FaBookmark /> : <FaRegBookmark />}
                </button>
              </div>
            </div>
            <div className="discussion-creator">
              <img src={discussion.creator.profileImage} alt={discussion.creator.name} className="creator-image" />
              <span className="creator-name">{discussion.creator.name}</span>
              <span className="creator-created-at">님이 개설한 토론</span>
            </div>
            <div className="discussion-meta">
              <div className="meta-item">
                <span className="meta-label">장소</span>
                <span className="meta-value">{discussion.location}</span>
              </div>
              <div className="meta-item">
                <span className="meta-label">인원</span>
                <span className="meta-value">{discussion.participantCount}/{discussion.maxParticipants}명</span>
              </div>
              <div className="meta-item">
                <span className="meta-label">일시</span>
                <span className="meta-value">
                  {formatDateTime(discussion.startDateTime)}
                </span>
              </div>
              <div className="meta-item">
                <span className="meta-label">시간</span>
                <span className="meta-value">
                  {new Date(discussion.startDateTime).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit', hour12: false })} ~ {new Date(discussion.endDateTime).toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit', hour12: false })}
                </span>
              </div>
            </div>
            <div className="discussion-participants">
              <h3>
                참여자 
                <span className="participant-count">
                  {discussion.participants.length}/{discussion.maxParticipants}명
                </span>
              </h3>
              <div className="participants-list">
                {discussion.participants.map(participant => (
                  <div key={participant.id} className="participant-item">
                    <span className="participant-name">{participant.name}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
          
          <div className="discussion-detail-content">
            <ReactMarkdown
              remarkPlugins={[remarkGfm]}
              components={{
                code({node, inline, className, children, ...props}) {
                  const match = /language-(\w+)/.exec(className || '');
                  return !inline && match ? (
                    <SyntaxHighlighter
                      style={vscDarkPlus}
                      language={match[1]}
                      PreTag="div"
                      {...props}
                    >
                      {String(children).replace(/\n$/, '')}
                    </SyntaxHighlighter>
                  ) : (
                    <code className={className} {...props}>
                      {children}
                    </code>
                  );
                }
              }}
            >
              {discussion.content}
            </ReactMarkdown>
          </div>

          <div className="discussion-join-section">
            <button 
              className="join-button" 
              onClick={handleJoin}
              disabled={joining || discussion.participantCount >= discussion.maxParticipants}
            >
              {joining ? '참여 중...' : 
               discussion.participantCount >= discussion.maxParticipants ? '인원 마감' : 
               '참여하기'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DiscussionDetailPage; 