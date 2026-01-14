# Guía para Probar Comunicación entre Microservicios con Postman

## 📋 Prerequisitos

1. **Keycloak** corriendo en `http://localhost:8080`
2. **Eureka Server** corriendo en `http://localhost:8761`
3. **StudentMS** corriendo en `http://localhost:8084`
4. **ReservationMS** corriendo en `http://localhost:8086`
5. **Postman** instalado

## 🚀 Pasos Rápidos

### Opción 1: Usar la Colección de Postman (Recomendado)

1. **Importar la colección:**
   - Abre Postman
   - Click en "Import" (arriba a la izquierda)
   - Selecciona el archivo `postman-collection.json`
   - La colección se importará con todas las peticiones configuradas

2. **Obtener el token automáticamente:**
   - Ejecuta la petición "1. OAuth2 - Get Token (Client Credentials)"
   - El token se guardará automáticamente en la variable `access_token`
   - Verás en la consola de Postman: "Token guardado automáticamente"

3. **Probar los endpoints:**
   - Ejecuta las demás peticiones en orden
   - Todas usarán automáticamente el token guardado

### Opción 2: Probar Manualmente

#### Paso 1: Obtener Token OAuth2

**Request:**
```
POST http://localhost:8080/realms/securityms/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded
```

**Body (x-www-form-urlencoded):**
```
grant_type: client_credentials
client_id: reservationms
client_secret: Eyt3YKuZWzj1zQY2rWxivpsfnYCmhfL8
scope: student.read
```

**Response esperado:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...",
  "expires_in": 300,
  "token_type": "Bearer",
  "scope": "student.read"
}
```

**⚠️ Copia el `access_token` para usarlo en los siguientes pasos**

---

#### Paso 2: Crear un Estudiante (Opcional)

**Request:**
```
POST http://localhost:8084/students
Content-Type: application/json
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Body (JSON):**
```json
{
  "firstName": "John",
  "school": "MIT",
  "age": 22,
  "inc": 123456
}
```

**Response esperado:**
```json
{
  "id": 1,
  "firstName": "John",
  "school": "MIT",
  "age": 22,
  "inc": 123456
}
```

---

#### Paso 3: Probar StudentMS Directamente

**Request:**
```
GET http://localhost:8084/students/1
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Response esperado:**
```json
{
  "id": 1,
  "firstName": "John",
  "school": "MIT",
  "age": 22,
  "inc": 123456
}
```

**✅ Si funciona:** El token tiene el rol `student.read` y StudentMS lo acepta.

**❌ Si falla con 403 Forbidden:** 
- Verifica que el rol `student.read` esté asignado al service account de `reservationms` en Keycloak
- Verifica que el token incluya el scope `student.read`

---

#### Paso 4: Probar ReservationMS (Comunicación MS-to-MS)

**Request:**
```
POST http://localhost:8086/reservations?studentId=1
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Response esperado:**
```json
{
  "idReservation": 1,
  "yearUniv": "2024-2025",
  "isValid": true,
  "studentId": 1
}
```

**✅ Si funciona:** 
- ReservationMS validó el token
- ReservationMS llamó internamente a StudentMS usando Feign
- StudentMS aceptó la petición con el token
- Se creó la reserva

**❌ Si falla con 502 Bad Gateway:**
- Verifica que StudentMS esté corriendo
- Verifica los logs de ReservationMS para ver el error de Feign

**❌ Si falla con 404 Not Found:**
- El estudiante con ID 1 no existe, crea uno primero (Paso 2)

---

#### Paso 5: Verificar la Reserva Creada

**Request:**
```
GET http://localhost:8086/reservations/1/student
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Response esperado:**
```json
{
  "id": 1,
  "firstName": "John",
  "school": "MIT",
  "age": 22,
  "inc": 123456
}
```

**✅ Si funciona:** ReservationMS llamó exitosamente a StudentMS para obtener la información del estudiante.

---

## 🔍 Verificar que Todo Funciona

### Checklist:

- [ ] Token OAuth2 se obtiene correctamente
- [ ] Token incluye el scope `student.read`
- [ ] StudentMS acepta peticiones con el token
- [ ] ReservationMS puede crear reservas
- [ ] ReservationMS puede obtener información del estudiante desde StudentMS

### Errores Comunes:

1. **401 Unauthorized:**
   - Token expirado (válido por 300 segundos)
   - Token no válido
   - Solución: Obtén un nuevo token

2. **403 Forbidden:**
   - Token no tiene el rol `student.read`
   - Solución: Verifica en Keycloak que el service account de `reservationms` tenga el rol asignado

3. **502 Bad Gateway:**
   - StudentMS no está corriendo
   - Error en la comunicación Feign
   - Solución: Verifica que StudentMS esté activo y revisa los logs

4. **404 Not Found:**
   - El estudiante/reserva no existe
   - Solución: Crea primero el estudiante/reserva

---

## 📝 Notas Importantes

1. **El token expira en 300 segundos (5 minutos)**
   - Si obtienes 401, obtén un nuevo token

2. **El token se propaga automáticamente**
   - ReservationMS usa Feign con OAuth2 interceptor
   - El token se añade automáticamente a las peticiones a StudentMS

3. **Verifica los logs**
   - En ReservationMS deberías ver: "Envoi du token OAuth2 au service student : ..."
   - Esto confirma que el interceptor está funcionando

4. **Configuración de Keycloak:**
   - Client `reservationms` debe tener Service Account habilitado
   - El rol `student.read` del client `studentms` debe estar asignado al service account de `reservationms`

---

## 🎯 Flujo Completo de Prueba

```
1. Obtener Token → 2. Crear Estudiante → 3. Verificar Estudiante → 
4. Crear Reserva → 5. Verificar Reserva → 6. Obtener Estudiante de Reserva
```

¡Todo debería funcionar si sigues estos pasos! 🚀
