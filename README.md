Two examples showing lazy loading fields with ORM and ORM Jakarta EE edition

To see the difference one needs to check the SQL logs

It requires a running PostgreSQL that can be started with:
```
sudo docker run --rm --name HibernateTestingPGSQL \
    -e POSTGRES_USER=hreact -e POSTGRES_PASSWORD=hreact -e POSTGRES_DB=hreact \
    -p 5432:5432 postgres:14.2
```

With ORM:

```
[Hibernate] 
    select
        book0_.id as id1_1_0_,
        book0_.author_id as author_i5_1_0_,
        book0_.isbn as isbn2_1_0_,
        book0_.title as title4_1_0_ 
    from
        books book0_ 
    where
        book0_.id=?
[Hibernate] 
    select
        book_.published as publishe3_1_ 
    from
        books book_ 
    where
        book_.id=?
'Cryptonomicon' was published in 1999
```

With ORM Jakarta:
```
[Hibernate] 
    select
        book0_.id as id1_1_0_,
        book0_.author_id as author_i5_1_0_,
        book0_.isbn as isbn2_1_0_,
        book0_.published as publishe3_1_0_,
        book0_.title as title4_1_0_ 
    from
        books book0_ 
    where
        book0_.id=?
'Cryptonomicon' was published in 1999
```

