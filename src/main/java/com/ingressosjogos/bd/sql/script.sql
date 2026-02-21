create  table Estadio(
	id serial primary key,
	nome varchar(40) not null,
	localizacao varchar(40),
	capacidade int
);

create table Torcedor(
	id serial primary key,
	nome VARCHAR(100) NOT NULL,
	email VARCHAR(120) UNIQUE NOT NULL,
	cpf VARCHAR(14) UNIQUE NOT NULL
);

create table Times(
	id serial primary key,
	nome varchar(700) not null unique
);

create table Jogo(
	id serial primary key,
	data_hora    TIMESTAMP NOT NULL,
    id_estadio   INTEGER NOT NULL,
    id_time_casa INTEGER NOT NULL,
    id_time_fora INTEGER NOT NULL,
    
    FOREIGN KEY (id_estadio)
    REFERENCES Estadio (id),

    FOREIGN KEY (id_time_casa)
    REFERENCES Times (id),


    FOREIGN KEY (id_time_fora)
    REFERENCES Times (id),

    CHECK (id_time_casa <> id_time_fora)
);

create table Ingresso (
	id_ingresso SERIAL PRIMARY KEY,
	preco       NUMERIC(10,2) NOT NULL,
	assento     VARCHAR(10) NOT NULL,
	status      VARCHAR(20) NOT NULL DEFAULT 'livre',
	id_jogo     INTEGER NOT NULL,
	id_torcedor INTEGER,

    FOREIGN KEY (id_jogo) REFERENCES Jogo (id),

    FOREIGN KEY (id_torcedor) REFERENCES Torcedor (id),


    CHECK (status IN ('livre','vendido')),

    UNIQUE (id_jogo, assento)
);

