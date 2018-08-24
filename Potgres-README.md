##Steps to run deploy and run postgres on k8s
--
1. Create config Map first

```
$ kubectl create configmap postgres-config --from-file=kubernetes-postgres-config-map.yaml
configmap/postgres-config created
```

2. List config maps

 ```
 $ kubectl get configmaps
 
 NAME              DATA      AGE
 
 postgres-config   1         18s
 ```
 
3. Describe config map

```
$ kubectl describe configmap postgres-config
Name:         postgres-config
Namespace:    apm
Labels:       <none>
Annotations:  <none>

Data
====
kubernetes-postgres-config-map.yaml:
----
apiVersion: v1
kind: ConfigMap
metadata:
  name: postgres-config
data:
  DB_HOST: postgres://postgres@postgres/sampledb?sslmode=disable
  DB_KIND: postgres
  PGDATA: /var/lib/postgresql/data/pgdata
  POSTGRES_USER: postgres
  POSTGRES_PASSWORD: s&~Tc@y34z<`N$rT
Events:  <none>

 ```
 
 4. Now create deployment for postgres here we are referencing the configmap just created.
 ```
 $ kubectl create -f postgres-deployment.yaml
 deployment.extensions/postgres created
 ```
 
 5. Now create service for postgres
 ```
  $ kubectl create -f postgres-service.yaml
 service/postgres created
 ```
 