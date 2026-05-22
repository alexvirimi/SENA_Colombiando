CREATE DATABASE IF NOT EXISTS colombiando_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE colombiando_db;

-- ── 1. CLIENTE ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS cliente (
    id_usuario        INT            NOT NULL AUTO_INCREMENT,
    nombre            VARCHAR(80)    NOT NULL,
    apellido          VARCHAR(80)    NOT NULL,
    correo            VARCHAR(120)   NOT NULL UNIQUE,
    telefono          VARCHAR(20),
    contrasena        VARCHAR(255)   NOT NULL,
    numero_documento  VARCHAR(30)    NOT NULL UNIQUE,
    tipo_documento    ENUM('CC','CE','PASAPORTE','NIT') NOT NULL DEFAULT 'CC',
    fecha_nacimiento  DATE,
    nacionalidad      VARCHAR(60)    NOT NULL DEFAULT 'Colombiana',
    fecha_registro    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_cliente PRIMARY KEY (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 2. EMPLEADO ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS empleado (
    id_usuario          INT            NOT NULL AUTO_INCREMENT,
    nombre              VARCHAR(80)    NOT NULL,
    apellido            VARCHAR(80)    NOT NULL,
    correo              VARCHAR(120)   NOT NULL UNIQUE,
    telefono            VARCHAR(20),
    contrasena          VARCHAR(255)   NOT NULL,
    cargo               VARCHAR(60)    NOT NULL,   -- GUIA, COORDINADOR, ASESOR
    salario             DECIMAL(12,2)  NOT NULL,
    fecha_contratacion  DATE           NOT NULL,
    activo              TINYINT(1)     NOT NULL DEFAULT 1,
    CONSTRAINT pk_empleado PRIMARY KEY (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 3. DESTINO ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS destino (
    id_destino    INT           NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(100)  NOT NULL,
    departamento  VARCHAR(80)   NOT NULL,
    municipio     VARCHAR(80)   NOT NULL,
    descripcion   TEXT,
    clima         VARCHAR(40),  -- Cálido, Frío, Templado, etc.
    imagen_url    VARCHAR(255),
    CONSTRAINT pk_destino PRIMARY KEY (id_destino)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 4. IDIOMA ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS idioma (
    id_idioma       INT          NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(60)  NOT NULL UNIQUE,
    codigo_iso      CHAR(5)      NOT NULL,   -- es, en, fr, pt…
    nivel_requerido ENUM('BASICO','INTERMEDIO','AVANZADO','NATIVO') NOT NULL DEFAULT 'BASICO',
    CONSTRAINT pk_idioma PRIMARY KEY (id_idioma)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 5. HABLA (relación N:M Empleado ↔ Idioma) ─────────────────
CREATE TABLE IF NOT EXISTS habla (
    id_empleado  INT          NOT NULL,
    id_idioma    INT          NOT NULL,
    nivel        ENUM('BASICO','INTERMEDIO','AVANZADO','NATIVO') NOT NULL DEFAULT 'BASICO',
    CONSTRAINT pk_habla        PRIMARY KEY (id_empleado, id_idioma),
    CONSTRAINT fk_habla_emp    FOREIGN KEY (id_empleado) REFERENCES empleado(id_usuario)
                                    ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_habla_idioma FOREIGN KEY (id_idioma)  REFERENCES idioma(id_idioma)
                                    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 6. TOUR ───────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS tour (
    id_tour          INT            NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(120)   NOT NULL,
    descripcion      TEXT,
    precio           DECIMAL(12,2)  NOT NULL,
    duracion_dias    SMALLINT       NOT NULL,
    capacidad_maxima SMALLINT       NOT NULL,
    fecha_salida     DATE           NOT NULL,
    fecha_regreso    DATE           NOT NULL,
    estado           ENUM('ACTIVO','CANCELADO','COMPLETO') NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_tour PRIMARY KEY (id_tour),
    CONSTRAINT chk_fechas CHECK (fecha_regreso >= fecha_salida)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 7. TOUR_DESTINO (relación N:M Tour ↔ Destino = PERTENECE) ─
CREATE TABLE IF NOT EXISTS tour_destino (
    id_tour     INT NOT NULL,
    id_destino  INT NOT NULL,
    CONSTRAINT pk_tour_destino       PRIMARY KEY (id_tour, id_destino),
    CONSTRAINT fk_td_tour            FOREIGN KEY (id_tour)    REFERENCES tour(id_tour)
                                         ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_td_destino         FOREIGN KEY (id_destino) REFERENCES destino(id_destino)
                                         ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 8. TOUR_EMPLEADO (relación N:M Tour ↔ Empleado) ──────────
CREATE TABLE IF NOT EXISTS tour_empleado (
    id_tour      INT NOT NULL,
    id_empleado  INT NOT NULL,
    rol_en_tour  VARCHAR(60) DEFAULT 'GUIA',
    CONSTRAINT pk_tour_empleado   PRIMARY KEY (id_tour, id_empleado),
    CONSTRAINT fk_te_tour         FOREIGN KEY (id_tour)     REFERENCES tour(id_tour)
                                      ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_te_empleado     FOREIGN KEY (id_empleado) REFERENCES empleado(id_usuario)
                                      ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 9. RESERVA ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS reserva (
    id_reserva        INT       NOT NULL AUTO_INCREMENT,
    id_cliente        INT       NOT NULL,
    id_tour           INT       NOT NULL,
    numero_pasajeros  SMALLINT  NOT NULL DEFAULT 1,
    fecha_reserva     DATE      NOT NULL,
    fecha_creacion    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado            ENUM('PENDIENTE','CONFIRMADA','CANCELADA','COMPLETADA')
                                NOT NULL DEFAULT 'PENDIENTE',
    observaciones     TEXT,
    CONSTRAINT pk_reserva          PRIMARY KEY (id_reserva),
    CONSTRAINT fk_reserva_cliente  FOREIGN KEY (id_cliente) REFERENCES cliente(id_usuario)
                                       ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_reserva_tour     FOREIGN KEY (id_tour)    REFERENCES tour(id_tour)
                                       ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_pasajeros       CHECK (numero_pasajeros >= 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── 10. PAGO ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS pago (
    id_pago       INT            NOT NULL AUTO_INCREMENT,
    id_reserva    INT            NOT NULL,
    monto         DECIMAL(12,2)  NOT NULL,
    fecha_pago    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metodo_pago   ENUM('EFECTIVO','TARJETA','TRANSFERENCIA','PSE','NEQUI')
                                 NOT NULL DEFAULT 'TRANSFERENCIA',
    estado        ENUM('PENDIENTE','APROBADO','RECHAZADO','REEMBOLSADO')
                                 NOT NULL DEFAULT 'PENDIENTE',
    referencia    VARCHAR(100),
    observaciones TEXT,
    CONSTRAINT pk_pago           PRIMARY KEY (id_pago),
    CONSTRAINT fk_pago_reserva   FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva)
                                     ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_monto         CHECK (monto > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE INDEX idx_cliente_correo   ON cliente(correo);
CREATE INDEX idx_cliente_doc      ON cliente(numero_documento);
CREATE INDEX idx_tour_estado      ON tour(estado);
CREATE INDEX idx_tour_salida      ON tour(fecha_salida);
CREATE INDEX idx_reserva_cliente  ON reserva(id_cliente);
CREATE INDEX idx_reserva_tour     ON reserva(id_tour);
CREATE INDEX idx_reserva_estado   ON reserva(estado);
CREATE INDEX idx_pago_reserva     ON pago(id_reserva);
CREATE INDEX idx_pago_estado      ON pago(estado);
