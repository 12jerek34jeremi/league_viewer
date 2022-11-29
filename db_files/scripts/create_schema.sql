CREATE TABLE countries (
    country_id INTEGER,
    name        VARCHAR2 (40 CHAR)
);

CREATE TABLE leagues (
    league_id INTEGER,
    name        VARCHAR2 (40 CHAR)
);

CREATE TABLE matches (
    match_id           INTEGER,
    match_date             DATE,
    stadium_id INTEGER,
    league_id INTEGER
);

CREATE TABLE players (
    player_id          INTEGER,
    name               VARCHAR2(40 CHAR),
    surname            VARCHAR2(40 CHAR),
    birth_date         DATE,
    birth_country_id INTEGER,
    team_id       INTEGER,
    height             INTEGER,
    weight             INTEGER
);

CREATE TABLE team_plays_in_league (
    team_id   INTEGER,
    league_id INTEGER
);

CREATE TABLE team_plays_in_match (
    team_id   INTEGER,
    match_id INTEGER,
    goal_amount    INTEGER
);

CREATE TABLE stadiums (
    stadium_id         INTEGER,
    name               VARCHAR2(40 CHAR),
    country_id  INTEGER
);

CREATE TABLE teams (
    team_id            INTEGER,
    name               VARCHAR2(40 CHAR),
    acronym            VARCHAR2(10 CHAR),
    country_id INTEGER
);


ALTER TABLE countries ADD CONSTRAINT country_pk PRIMARY KEY(country_id);
ALTER TABLE leagues ADD CONSTRAINT league_pk PRIMARY KEY(league_id);
ALTER TABLE matches ADD CONSTRAINT match_pk PRIMARY KEY(match_id);
ALTER TABLE players ADD CONSTRAINT player_pk PRIMARY KEY(player_id);
ALTER TABLE team_plays_in_league ADD CONSTRAINT team_plays_in_league_pk PRIMARY KEY(team_id, league_id);
ALTER TABLE team_plays_in_match ADD CONSTRAINT team_plays_in_match_pk PRIMARY KEY(team_id, match_id);
ALTER TABLE stadiums ADD CONSTRAINT stadium_pk PRIMARY KEY(stadium_id);
ALTER TABLE teams ADD CONSTRAINT team_pk PRIMARY KEY(team_id);

ALTER TABLE matches
    ADD CONSTRAINT match_plays_on_stadium_fk FOREIGN KEY (stadium_id)
        REFERENCES stadiums (stadium_id);
ALTER TABLE matches
    ADD CONSTRAINT match_plays_in_league_fk FOREIGN KEY (league_id)
        REFERENCES leagues (league_id);
ALTER TABLE players
    ADD CONSTRAINT birth_country_fk FOREIGN KEY (birth_country_id)
        REFERENCES countries (country_id);
ALTER TABLE players
    ADD CONSTRAINT plays_in_team_fk FOREIGN KEY (team_id)
        REFERENCES teams (team_id);
ALTER TABLE team_plays_in_league
    ADD CONSTRAINT team_in_league_fk FOREIGN KEY (team_id)
        REFERENCES teams (team_id);
ALTER TABLE team_plays_in_league
    ADD CONSTRAINT league_of_team_fk FOREIGN KEY (league_id)
        REFERENCES leagues (league_id);
ALTER TABLE team_plays_in_match
    ADD CONSTRAINT playing_team_fk FOREIGN KEY (team_id)
        REFERENCES teams (team_id);
ALTER TABLE team_plays_in_match
    ADD CONSTRAINT match_of_teams_fk FOREIGN KEY (match_id)
        REFERENCES matches (match_id);
ALTER TABLE stadiums
    ADD CONSTRAINT stadium_in_country_fk FOREIGN KEY (country_id)
        REFERENCES countries (country_id);
ALTER TABLE teams
    ADD CONSTRAINT team_in_country_fk FOREIGN KEY (country_id)
        REFERENCES countries (country_id);
