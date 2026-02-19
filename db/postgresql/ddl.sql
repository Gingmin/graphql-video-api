DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users
(
    id bigserial primary key,
    email varchar(255) not null unique,
    latest_login_ip varchar(45),
    last_login_date timestamptz,
    jti uuid,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN users.id IS 'id';
COMMENT ON COLUMN users.email IS '이메일';
COMMENT ON COLUMN users.latest_login_ip IS '최근 로그인 IP';
COMMENT ON COLUMN users.last_login_date IS '마지막 로그인 일시';
COMMENT ON COLUMN users.jti IS 'jti';
COMMENT ON COLUMN users.created_at IS '생성일';
COMMENT ON COLUMN users.modified_at IS '수정일';
COMMENT ON COLUMN users.is_deleted IS '삭제여부';
COMMENT ON TABLE users IS '유저';

DROP TABLE IF EXISTS profiles CASCADE;
CREATE TABLE profiles
(
    id bigserial primary key,
    user_id bigint not null references users(id) on delete cascade,
    name varchar(255) not null,
    profile_type varchar(15) not null check (profile_type in ('GENERAL', 'KIDS')),
    thumbnail_file_id bigint references files(id) on delete set null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (user_id, name)
);
COMMENT ON COLUMN profiles.id IS 'id';
COMMENT ON COLUMN profiles.user_id IS '유저 (아이디)';
COMMENT ON COLUMN profiles.name IS '이름';
COMMENT ON COLUMN profiles.profile_type IS '프로필 타입 (일반, 키즈)';
COMMENT ON COLUMN profiles.thumbnail_file_id IS '썸네일 사진 (아이디)';
COMMENT ON COLUMN profiles.created_at IS '생성일';
COMMENT ON COLUMN profiles.modified_at IS '수정일';
COMMENT ON COLUMN profiles.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_profiles_user_id_and_name ON profiles IS '유저 아이디와 이름 고유 제약조건';
COMMENT ON TABLE profiles IS '프로필';

DROP TABLE IF EXISTS wishlists CASCADE;
CREATE TABLE wishlists
(
    id bigserial primary key,
    content_id bigint not null references contents(id) on delete cascade,
    profile_id bigint not null references profiles(id) on delete cascade,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (content_id, profile_id)
);
COMMENT ON COLUMN wishlists.id IS 'id';
COMMENT ON COLUMN wishlists.content_id IS '콘텐츠 (아이디)';
COMMENT ON COLUMN wishlists.profile_id IS '유저 프로필 (아이디)';
COMMENT ON COLUMN wishlists.created_at IS '생성일';
COMMENT ON COLUMN wishlists.modified_at IS '수정일';
COMMENT ON COLUMN wishlists.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_wishlists_content_id_and_profile_id ON wishlists IS '콘텐츠 아이디와 유저 프로필 아이디 고유 제약조건';
COMMENT ON TABLE wishlists IS '찜목록';

DROP TABLE IF EXISTS evaluation_codes CASCADE;
CREATE TABLE evaluation_codes
(
    id bigserial primary key,
    evaluation varchar(15) not null check (evaluation in ('EXCELLENT', 'LIKE', 'DISLIKE')),
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (evaluation)
);
COMMENT ON COLUMN evaluation_codes.id IS 'id';
COMMENT ON COLUMN evaluation_codes.evaluation IS '평가';
COMMENT ON COLUMN evaluation_codes.created_at IS '생성일';
COMMENT ON COLUMN evaluation_codes.modified_at IS '수정일';
COMMENT ON COLUMN evaluation_codes.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_evaluation_codes_evaluation ON evaluation_codes IS '평가 코드 고유 제약조건';
COMMENT ON TABLE evaluation_codes IS '평가 코드';

INSERT INTO evaluation_codes (evaluation)
values ('EXCELLENT'), ('LIKE'), ('DISLIKE');

DROP TABLE IF EXISTS evaluations CASCADE;
CREATE TABLE evaluations
(
    id bigserial primary key,
    evaluation_codes_id bigint not null references evaluation_codes(id) on delete cascade,
    content_id bigint not null references contents(id) on delete cascade,
    profile_id bigint not null references profiles(id) on delete cascade,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (content_id, profile_id)
);
COMMENT ON COLUMN evaluations.id IS 'id';
COMMENT ON COLUMN evaluations.evaluation_codes_id IS '평가 코드 (아이디)';
COMMENT ON COLUMN evaluations.content_id IS '콘텐츠 (아이디)';
COMMENT ON COLUMN evaluations.profile_id IS '유저 프로필 (아이디)';
COMMENT ON COLUMN evaluations.created_at IS '생성일';
COMMENT ON COLUMN evaluations.modified_at IS '수정일';
COMMENT ON COLUMN evaluations.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_evaluations_content_id_and_profile_id ON evaluations IS '콘텐츠 아이디와 유저 프로필 아이디 고유 제약조건';
COMMENT ON TABLE evaluations IS '평가';

DROP TABLE IF EXISTS files CASCADE;
CREATE TABLE files
(
    id bigserial primary key,
    name varchar(255) not null,
    original_name varchar(255) not null,
    path text not null,
    extension varchar(10) not null,
    mime_type varchar(100) not null,
    file_size bigint not null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN files.id IS 'id';
COMMENT ON COLUMN files.name IS '이름';
COMMENT ON COLUMN files.original_name IS '원본 이름';
COMMENT ON COLUMN files.path IS '저장 위치';
COMMENT ON COLUMN files.extension IS '확장자';
COMMENT ON COLUMN files.mime_type IS 'mime 타입';
COMMENT ON COLUMN files.file_size IS '크기';
COMMENT ON COLUMN files.created_at IS '생성일';
COMMENT ON COLUMN files.modified_at IS '수정일';
COMMENT ON COLUMN files.is_deleted IS '삭제여부';
COMMENT ON TABLE files IS '파일';

DROP TABLE IF EXISTS subtitles CASCADE;
CREATE TABLE subtitles
(
    id bigserial primary key,
    language_type varchar(10) not null check (language_type in ('KO', 'EN')),
    subtitle_file_id bigint not null references files(id) on delete cascade,
    video_id bigint references videos(id) on delete set null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (language_type, subtitle_file_id, video_id)
);
COMMENT ON COLUMN subtitles.id IS 'id';
COMMENT ON COLUMN subtitles.language_type IS '언어 타입';
COMMENT ON COLUMN subtitles.subtitle_file_id IS '파일 (아이디)';
COMMENT ON COLUMN subtitles.video_id IS '비디오 (아이디)';
COMMENT ON COLUMN subtitles.created_at IS '생성일';
COMMENT ON COLUMN subtitles.modified_at IS '수정일';
COMMENT ON COLUMN subtitles.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_subtitles_language_type_and_subtitle_file_id_and_video_id ON subtitles IS '언어 타입, 파일 아이디, 비디오 아이디 고유 제약조건';
COMMENT ON TABLE subtitles IS '자막';

DROP TABLE IF EXISTS videos CASCADE;
CREATE TABLE videos
(
    id bigserial primary key,
    video_file_id bigint not null references files(id) on delete cascade,
    cumulative_viewing_time bigint,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN videos.id IS 'id';
COMMENT ON COLUMN videos.video_file_id IS '비디오 파일 (아이디)';
COMMENT ON COLUMN videos.cumulative_viewing_time IS '누적 시청시간';
COMMENT ON COLUMN videos.created_at IS '생성일';
COMMENT ON COLUMN videos.modified_at IS '수정일';
COMMENT ON COLUMN videos.is_deleted IS '삭제여부';
COMMENT ON TABLE videos IS '비디오';

DROP TABLE IF EXISTS contents CASCADE;
CREATE TABLE contents
(
    id bigserial primary key,
    content_type varchar(15) not null check (content_type in ('MOVIE', 'SERIES')),
    title varchar(255) not null,
    description text,
    age_rating varchar(10) not null check (age_rating in ('ALL', '12', '15', '19')),
    thumbnail_file_id bigint references files(id) on delete set null,
    trailer_file_id bigint references files(id) on delete set null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN contents.id IS 'id';
COMMENT ON COLUMN contents.content_type IS '컨텐츠 타입';
COMMENT ON COLUMN contents.title IS '제목';
COMMENT ON COLUMN contents.description IS '설명';
COMMENT ON COLUMN contents.age_rating IS '관람등급';
COMMENT ON COLUMN contents.thumbnail_file_id IS '썸네일 파일 (아이디)';
COMMENT ON COLUMN contents.trailer_file_id IS '예고편 파일 (아이디)';
COMMENT ON COLUMN contents.created_at IS '생성일';
COMMENT ON COLUMN contents.modified_at IS '수정일';
COMMENT ON COLUMN contents.is_deleted IS '삭제여부';
COMMENT ON TABLE contents IS '콘텐츠';

DROP TABLE IF EXISTS movies CASCADE;
CREATE TABLE movies
(
    id bigserial primary key,
    content_id bigint not null references contents(id) on delete cascade,
    duration_seconds integer not null,
    video_id bigint not null references videos(id) on delete cascade,
    release_date date,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN movies.id IS 'id';
COMMENT ON COLUMN movies.content_id IS '콘텐츠 (아이디)';
COMMENT ON COLUMN movies.duration_seconds IS '지속시간 (초)';
COMMENT ON COLUMN movies.video_id IS '비디오 (아이디)';
COMMENT ON COLUMN movies.release_date IS '출시일';
COMMENT ON COLUMN movies.created_at IS '생성일';
COMMENT ON COLUMN movies.modified_at IS '수정일';
COMMENT ON COLUMN movies.is_deleted IS '삭제여부';
COMMENT ON TABLE movies IS '영화';

DROP TABLE IF EXISTS series CASCADE;
CREATE TABLE series
(
    id bigserial primary key,
    content_id bigint not null references contents(id) on delete cascade,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN series.id IS 'id';
COMMENT ON COLUMN series.content_id IS '콘텐츠 (아이디)';
COMMENT ON COLUMN series.created_at IS '생성일';
COMMENT ON COLUMN series.modified_at IS '수정일';
COMMENT ON COLUMN series.is_deleted IS '삭제여부';
COMMENT ON TABLE series IS '시리즈';

DROP TABLE IF EXISTS seasons CASCADE;
CREATE TABLE seasons
(
    id bigserial primary key,
    series_id bigint not null references series(id) on delete cascade,
    season_number integer not null,
    title varchar(255) not null,
    description text,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (series_id, season_number)
);
COMMENT ON COLUMN seasons.id IS 'id';
COMMENT ON COLUMN seasons.series_id IS '시리즈 (아이디)';
COMMENT ON COLUMN seasons.season_number IS '시즌 회차';
COMMENT ON COLUMN seasons.title IS '시즌 제목';
COMMENT ON COLUMN seasons.description IS '시즌 설명';
COMMENT ON COLUMN seasons.created_at IS '생성일';
COMMENT ON COLUMN seasons.modified_at IS '수정일';
COMMENT ON COLUMN seasons.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_seasons_series_id_and_season_number ON seasons IS '시리즈 아이디와 시즌 회차 고유 제약조건';
COMMENT ON TABLE seasons IS '시즌';

DROP TABLE IF EXISTS episodes CASCADE;
CREATE TABLE episodes
(
    id bigserial primary key,
    season_id bigint not null references seasons(id) on delete cascade,
    episode_number integer not null,
    title varchar(255) not null,
    description text,
    duration_seconds integer not null,
    video_id bigint not null references videos(id) on delete cascade,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (season_id, episode_number)
);
COMMENT ON COLUMN episodes.id IS 'id';
COMMENT ON COLUMN episodes.season_id IS '시즌 (아이디)';
COMMENT ON COLUMN episodes.episode_number IS '에피소드 회차';
COMMENT ON COLUMN episodes.title IS '제목';
COMMENT ON COLUMN episodes.description IS '설명';
COMMENT ON COLUMN episodes.duration_seconds IS '지속시간';
COMMENT ON COLUMN episodes.video_id IS '비디오 (아이디)';
COMMENT ON COLUMN episodes.created_at IS '생성일';
COMMENT ON COLUMN episodes.modified_at IS '수정일';
COMMENT ON COLUMN episodes.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_episodes_season_id_and_episode_number ON episodes IS '시즌 아이디와 에피소드 회차 고유 제약조건';
COMMENT ON TABLE episodes IS '에피소드';

DROP TABLE IF EXISTS genres CASCADE;
CREATE TABLE genres
(
    id bigserial primary key,
    code varchar(50) not null unique,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN genres.id IS 'id';
COMMENT ON COLUMN genres.code IS '코드';
COMMENT ON COLUMN genres.created_at IS '생성일';
COMMENT ON COLUMN genres.modified_at IS '수정일';
COMMENT ON COLUMN genres.is_deleted IS '삭제여부';
COMMENT ON TABLE genres IS '장르';

DROP TABLE IF EXISTS content_genres CASCADE;
CREATE TABLE content_genres
(
    id bigserial primary key,
    content_id bigint not null references contents(id) on delete cascade,
    genre_id bigint not null references genres(id) on delete cascade,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (content_id, genre_id)
);
COMMENT ON COLUMN content_genres.id IS 'id';
COMMENT ON COLUMN content_genres.content_id IS '콘텐츠 (아이디)';
COMMENT ON COLUMN content_genres.genre_id IS '장르 (아이디)';
COMMENT ON COLUMN content_genres.created_at IS '생성일';
COMMENT ON COLUMN content_genres.modified_at IS '수정일';
COMMENT ON COLUMN content_genres.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_content_genres_content_id_and_genre_id ON content_genres IS '콘텐츠 아이디와 장르 아이디 고유 제약조건';
COMMENT ON TABLE content_genres IS '콘텐츠_장르';

DROP TABLE IF EXISTS genre_translation CASCADE;
CREATE TABLE genre_translation
(
    id bigserial primary key,
    genre_id bigint not null references genres(id) on delete cascade,
    language varchar(10) not null check (language in ('KO', 'EN')),
    name varchar(255) not null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (genre_id, language)
);
COMMENT ON COLUMN genre_translation.id IS 'id';
COMMENT ON COLUMN genre_translation.genre_id IS '장르 (아이디)';
COMMENT ON COLUMN genre_translation.language IS '언어';
COMMENT ON COLUMN genre_translation.name IS '이름';
COMMENT ON COLUMN genre_translation.created_at IS '생성일';
COMMENT ON COLUMN genre_translation.modified_at IS '수정일';
COMMENT ON COLUMN genre_translation.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_genre_translation_genre_id_and_language ON genre_translation IS '장르 아이디와 언어 고유 제약조건';
COMMENT ON TABLE genre_translation IS '장르 번역';

DROP TABLE IF EXISTS tags CASCADE;
CREATE TABLE tags
(
    id bigserial primary key,
    code varchar(50) not null unique,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN tags.id IS 'id';
COMMENT ON COLUMN tags.code IS '코드';
COMMENT ON COLUMN tags.created_at IS '생성일';
COMMENT ON COLUMN tags.modified_at IS '수정일';
COMMENT ON COLUMN tags.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_tags_code ON tags IS '코드 고유 제약조건';
COMMENT ON TABLE tags IS '태그';

DROP TABLE IF EXISTS content_tags CASCADE;
CREATE TABLE content_tags
(
    id bigserial primary key,
    content_id bigint not null references contents(id) on delete cascade,
    tag_id bigint not null references tags(id) on delete cascade,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (content_id, tag_id)
);
COMMENT ON COLUMN content_tags.id IS 'id';
COMMENT ON COLUMN content_tags.content_id IS '콘텐츠 (아이디)';
COMMENT ON COLUMN content_tags.tag_id IS '태그 (아이디)';
COMMENT ON COLUMN content_tags.created_at IS '생성일';
COMMENT ON COLUMN content_tags.modified_at IS '수정일';
COMMENT ON COLUMN content_tags.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_content_tags_content_id_and_tag_id ON content_tags IS '콘텐츠 아이디와 태그 아이디 고유 제약조건';
COMMENT ON TABLE content_tags IS '콘텐츠_태그';

DROP TABLE IF EXISTS tag_translation CASCADE;
CREATE TABLE tag_translation
(
    id bigserial primary key,
    tag_id bigint not null references tags(id) on delete cascade,
    language varchar(10) not null check (language in ('KO', 'EN')),
    name varchar(255) not null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (tag_id, language)
);
COMMENT ON COLUMN tag_translation.id IS 'id';
COMMENT ON COLUMN tag_translation.tag_id IS '태그 (아이디)';
COMMENT ON COLUMN tag_translation.language IS '언어';
COMMENT ON COLUMN tag_translation.name IS '이름';
COMMENT ON COLUMN tag_translation.created_at IS '생성일';
COMMENT ON COLUMN tag_translation.modified_at IS '수정일';
COMMENT ON COLUMN tag_translation.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_tag_translation_tag_id_and_language ON tag_translation IS '태그 아이디와 언어 고유 제약조건';
COMMENT ON TABLE tag_translation IS '태그 번역';

DROP TABLE IF EXISTS persons CASCADE;
CREATE TABLE persons
(
    id bigserial primary key,
    code varchar(50) not null unique,
    birth_date date,
    nationality varchar(50),
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN persons.id IS 'id';
COMMENT ON COLUMN persons.code IS '코드';
COMMENT ON COLUMN persons.birth_date IS '생일';
COMMENT ON COLUMN persons.nationality IS '국적';
COMMENT ON COLUMN persons.created_at IS '생성일';
COMMENT ON COLUMN persons.modified_at IS '수정일';
COMMENT ON COLUMN persons.is_deleted IS '삭제여부';
COMMENT ON TABLE persons IS '인물';

DROP TABLE IF EXISTS person_translation CASCADE;
CREATE TABLE person_translation
(
    id bigserial primary key,
    person_id bigint not null references persons(id) on delete cascade,
    language varchar(10) not null check (language in ('KO', 'EN')),
    name varchar(255) not null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (person_id, language)
);
COMMENT ON COLUMN person_translation.id IS 'id';
COMMENT ON COLUMN person_translation.person_id IS '인물 (아이디)';
COMMENT ON COLUMN person_translation.language IS '언어';
COMMENT ON COLUMN person_translation.name IS '이름';
COMMENT ON COLUMN person_translation.created_at IS '생성일';
COMMENT ON COLUMN person_translation.modified_at IS '수정일';
COMMENT ON COLUMN person_translation.is_deleted IS '삭제 여부';
COMMENT ON CONSTRAINT uq_person_translation_person_id_and_language ON person_translation IS '인물 아이디와 언어 고유 제약조건';
COMMENT ON TABLE person_translation IS '인물 번역';

DROP TABLE IF EXISTS role_type_codes CASCADE;
CREATE TABLE role_type_codes 
(
    id bigserial primary key,
    code varchar(50) not null unique check (code in ('ACTOR', 'DIRECTOR', 'SCREENWRITER')),
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN role_type_codes.id IS 'id';
COMMENT ON COLUMN role_type_codes.code IS '코드';
COMMENT ON COLUMN role_type_codes.created_at IS '생성일';
COMMENT ON COLUMN role_type_codes.modified_at IS '수정일';
COMMENT ON COLUMN role_type_codes.is_deleted IS '삭제 여부';
COMMENT ON TABLE role_type_codes IS '역할 타입 코드';

INSERT INTO role_type_codes (code) VALUES ('ACTOR'), ('DIRECTOR'), ('SCREENWRITER');

DROP TABLE IF EXISTS content_persons CASCADE;
CREATE TABLE content_persons
(
    id bigserial primary key,
    content_id bigint not null references contents(id) on delete cascade,
    person_id bigint not null references persons(id) on delete cascade,
    role_type_code_id bigint not null references role_type_codes(id) on delete cascade,
    role_name varchar(255),
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false
);
COMMENT ON COLUMN content_persons.id IS 'id';
COMMENT ON COLUMN content_persons.content_id IS '콘텐츠 (아이디)';
COMMENT ON COLUMN content_persons.person_id IS '인물 (아이디)';
COMMENT ON COLUMN content_persons.role_type_code_id IS '역할 타입 코드 (아이디)';
COMMENT ON COLUMN content_persons.role_name IS '배역 이름';
COMMENT ON COLUMN content_persons.created_at IS '생성일';
COMMENT ON COLUMN content_persons.modified_at IS '수정일';
COMMENT ON COLUMN content_persons.is_deleted IS '삭제 여부';
COMMENT ON TABLE content_persons IS '콘텐츠_인물';

DROP TABLE IF EXISTS episode_watching CASCADE;
CREATE TABLE episode_watching
(
    id bigserial primary key,
    profile_id bigint not null references profiles(id) on delete cascade,
    episode_id bigint not null references episodes(id) on delete cascade,
    timeline integer not null,
    subtitle_id bigint references subtitles(id) on delete set null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (profile_id, episode_id)
);
COMMENT ON COLUMN episode_watching.id IS 'id';
COMMENT ON COLUMN episode_watching.profile_id IS '유저 프로필 (아이디)';
COMMENT ON COLUMN episode_watching.episode_id IS '에피소드 (아이디)';
COMMENT ON COLUMN episode_watching.timeline IS '시간';
COMMENT ON COLUMN episode_watching.subtitle_id IS '자막 (아이디)';
COMMENT ON COLUMN episode_watching.created_at IS '생성일';
COMMENT ON COLUMN episode_watching.modified_at IS '수정일';
COMMENT ON COLUMN episode_watching.is_deleted IS '삭제여부';
COMMENT ON CONSTRAINT uq_episode_watching_profile_id_and_episode_id ON episode_watching IS '유저 프로필 아이디와 에피소드 아이디 고유 제약조건';
COMMENT ON TABLE episode_watching IS '에피소드 시청중';

DROP TABLE IF EXISTS movie_watching CASCADE;
CREATE TABLE movie_watching
(
    id bigserial primary key,
    profile_id bigint not null references profiles(id) on delete cascade,
    movie_id bigint not null references movies(id) on delete cascade,
    timeline integer not null,
    subtitle_id bigint references subtitles(id) on delete set null,
    created_at timestamptz not null default now(),
    modified_at timestamptz not null default now(),
    is_deleted boolean not null default false,
    UNIQUE (profile_id, movie_id)
);
COMMENT ON COLUMN movie_watching.id IS 'id';
COMMENT ON COLUMN movie_watching.created_at IS '생성일';
COMMENT ON COLUMN movie_watching.modified_at IS '수정일';
COMMENT ON COLUMN movie_watching.is_deleted IS '삭제여부';
COMMENT ON COLUMN movie_watching.timeline IS '시간';
COMMENT ON COLUMN movie_watching.subtitle_id IS '자막 (아이디)';
COMMENT ON COLUMN movie_watching.movie_id IS '영화 (아이디)';
COMMENT ON COLUMN movie_watching.profile_id IS '유저 프로필 (아이디)';
COMMENT ON TABLE movie_watching IS '영화 시청중';
COMMENT ON CONSTRAINT uq_movie_watching_profile_id_and_movie_id ON movie_watching IS '유저 프로필 아이디와 영화 아이디 고유 제약조건';

CREATE INDEX idx_contents_content_type ON contents(content_type);
CREATE INDEX idx_wishlists_profile_id ON wishlists(profile_id);
CREATE INDEX idx_evaluations_profile_id ON evaluations(profile_id);
CREATE INDEX idx_episode_watching_profile_id ON episode_watching(profile_id);
CREATE INDEX idx_movie_watching_profile_id ON movie_watching(profile_id);
CREATE INDEX idx_profiles_user_id ON profiles(user_id);