-- RFC1: Historial de viajes de un usuario servicio
SELECT 
    -- Información del Viaje   
    V.ID AS ID_VIAJE,
    V.HORA_INICIO,
    V.HORA_FIN,
    V.DISTANCIA_KM,
    V.COSTO_TOTAL,    
    -- Información del Pasajero (Usuario de Servicio)
    UP.NOMBRE AS NOMBRE_PASAJERO,
    UP.EMAIL AS EMAIL_PASAJERO,    
    -- Información del Conductor
    UC.NOMBRE AS NOMBRE_CONDUCTOR,
    UC.EMAIL AS EMAIL_CONDUCTOR,    
    -- Información del Vehículo
    VEH.MARCA AS VEHICULO_MARCA,
    VEH.MODELO AS VEHICULO_MODELO,
    VEH.PLACA AS VEHICULO_PLACA,
    VEH.NIVEL AS VEHICULO_NIVEL,    
    -- Puntos Geográficos
    PI.DIRECCION AS DIRECCION_INICIO,
    PF.DIRECCION AS DIRECCION_FIN,    
    -- Calificación y Revisión
    R.CALIFICACION,
    R.COMENTARIO AS COMENTARIO_REVISION    
FROM 
    VIAJE V
-- 1. Unir con el usuario que pidió el servicio (Pasajero)
JOIN 
    USUARIO_SERVICIO US ON V.ID_USUARIO_SERVICIO = US.ID
JOIN 
    USUARIO UP ON US.ID = UP.ID  -- UP: Usuario Pasajero
    
-- 2. Unir con el Conductor
JOIN 
    USUARIO_CONDUCTOR COND ON V.ID_USUARIO_CONDUCTOR = COND.ID
JOIN 
    USUARIO UC ON COND.ID = UC.ID -- UC: Usuario Conductor
    
-- 3. Unir con el Vehículo
LEFT JOIN 
    VEHICULO VEH ON V.ID_VEHICULO = VEH.ID
    
-- 4. Unir con los Puntos Geográficos (Inicio y Fin)
JOIN 
    PUNTO_GEOGRAFICO PI ON V.ID_PUNTO_INICIO = PI.ID  -- PI: Punto Inicio
JOIN 
    PUNTO_GEOGRAFICO PF ON V.ID_PUNTO_FIN = PF.ID     -- PF: Punto Fin

-- 5. Unir con la Revisión (puede no existir, por eso usamos LEFT JOIN)
LEFT JOIN 
    REVISION R ON V.ID = R.ID_VIAJE
    
WHERE 
    --FILTRO por usuario servicio específico
    V.ID_USUARIO_SERVICIO = 2 
    
ORDER BY V.HORA_INICIO DESC;

-- RFC2: Top 20 conductores con más servicios
SELECT u.ID_USUARIO_CONDUCTOR AS CONDUCTOR_ID, usun.NOMBRE, COUNT(*) AS TOTAL_SERVICIOS
FROM VIAJE u
JOIN USUARIO_CONDUCTOR uc ON uc.ID = u.ID_USUARIO_CONDUCTOR
JOIN USUARIO usun ON usun.ID = uc.ID
GROUP BY u.ID_USUARIO_CONDUCTOR, usun.NOMBRE
ORDER BY TOTAL_SERVICIOS DESC
FETCH FIRST 20 ROWS ONLY;


-- RFC3: Total dinero obtenido por conductor por vehículo y por servicio
-- (60% para el conductor)
SELECT v.ID_USUARIO_CONDUCTOR,
ve.ID AS VEHICULO_ID,
s.ID AS SERVICIO_ID,
s.TIPO, s.NIVEL,
SUM(NVL(v.PRECIO,0) * 0.6) AS TOTAL_CONDUCTOR
FROM VIAJE v
JOIN VEHICULO ve ON ve.ID = v.VEHICULO_ID
LEFT JOIN SERVICIO s ON s.ID = v.SERVICIO_ID
GROUP BY v.ID_USUARIO_CONDUCTOR, ve.ID, s.ID, s.TIPO, s.NIVEL
ORDER BY v.ID_USUARIO_CONDUCTOR, ve.ID;


-- RFC4: Utilización de servicios en una ciudad y rango de fechas
-- :ciudadId, :desde (timestamp), :hasta (timestamp)
WITH base AS (
SELECT s.TIPO, s.NIVEL, COUNT(*) AS CNT
FROM VIAJE v
JOIN SERVICIO s ON s.ID = v.SERVICIO_ID
JOIN PUNTO_GEOGRAFICO p ON p.ID = v.ORIGEN_ID
WHERE v.FECHA_SALIDA >= :desde AND v.FECHA_SALIDA < :hasta
AND p.CIUDAD_ID = :ciudadId
GROUP BY s.TIPO, s.NIVEL
)
SELECT b.TIPO, b.NIVEL, b.CNT,
ROUND( (b.CNT / SUM(b.CNT) OVER()) * 100, 2) AS PORCENTAJE
FROM base b
ORDER BY b.CNT DESC;