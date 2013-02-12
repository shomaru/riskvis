CREATE TABLE forums (
    forumtitle character varying(255),
    forumuri character varying(255) NOT NULL
);

ALTER TABLE forums
    ADD CONSTRAINT forums_pkey PRIMARY KEY (forumuri);

CREATE TABLE threads (
    threaduri character varying(255) NOT NULL,
    threadtitle character varying(255),
    status character varying(255),
    noofviews double precision,
    forumuri character varying(255) NOT NULL
);

ALTER TABLE threads
    ADD CONSTRAINT threads_pkey PRIMARY KEY (threaduri, forumuri);

CREATE TABLE messages (
    messageuri character varying(255) NOT NULL,
    threaduri character varying(255),
    messagetitle character varying(255),
    contributor integer,
    creationdate character varying(255)
);

ALTER TABLE messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (messageuri);

CREATE TABLE message_points (
    messageuri character varying(255) NOT NULL,
    awardedpoints integer,
    comment text
);

ALTER TABLE message_points
    ADD CONSTRAINT message_points_pkey PRIMARY KEY (messageuri);