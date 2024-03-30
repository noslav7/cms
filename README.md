The repository contains the application for Customer Management System (CMS). It is a part of Customer
 Management System (CRM) of an industrial enterprise, which produces industrial equipment.
 CMS in this app represents itself as a system to collect and store data about customers (organizations in this case),
various contact data, data about interactions with the customers and the contacts.
Another app, that logs data about each action of the CMS is called "stub". It's repository may be found at
https://github.com/noslav7/stub

JSONs for testing (e.g using postman)

POST      localhost:8080/cms/customers

 {
    "organisation": "SvarTechInvest",
    "city": "Saint-Petersburg",
    "industry": "reselling"
  }
  {
    "organisation": "UralTermoSvar",
    "city": "Yekaterinburg",
    "industry": "welding_equipment_production"
  }
  {
    "organisation": "GlobalStroiIngeneering",
    "city": "Nizhnii Novgorod",
    "industry": "pipeline_petroleum_transportation"
  }
  {
    "organisation": "TMZ",
    "city": "Tikhvin",
    "industry": "mechanical_engeneering"
  }

  GET      localhost:8080/cms/customers/2

  GET      localhost:8080/cms/customers/industries/reselling

  GET      localhost:8080/cms/customers

  PUT      localhost:8080/cms/customers/1
  
  {
    "organisation": "STI",
    "city": "Saint-Petersburg",
    "industry": "reselling"
  }

  DELETE   localhost:8080/cms/customers/3





  POST      localhost:8080/cms/contacts
  
  {
    "customer_id": 1,
    "name": "Ivanov Dmitriy",
    "type": "email",
    "details": "ivanov@svartech.ru",
    "preferred": true
  }
  
  {
    "customer_id": 1,
    "name": "Arsentev Petr",
    "type": "email",
    "details": "rpa@svartech.ru",
    "preferred": false
  }
  
  {
    "customer_id": 2,
    "name": "Ezdakov Yuriy",
    "type": "telephone",
    "details": "83437654321",
    "preferred": true
  }
  
  {
    "customer_id": 3,
    "name": "Dukin Vladimir",
    "type": "telephone",
    "details": "88317654321",
    "preferred": true
  }

  GET      localhost:8080/cms/contacts/3

  GET      localhost:8080/cms/contacts/customers/2

  GET      localhost:8080/cms/contacts

  PUT      localhost:8080/cms/contacts/2
  
  {
    "customer_id": 2,
    "name": "Korobeinikov Stas",
    "type": "email",
    "details": "ks@svartech.ru",
    "preferred": true
  }

  DELETE   localhost:8080/cms/contacts/4




  POST      localhost:8080/cms/interactions
  
  {
    "customer_id": 1,
    "contact_id": 1,
    "date": "2024-01-10",
    "type": "payment",
    "notes": "received 2_500_000"
  }
  
  {
    "customer_id": 1,
    "contact_id": 1,
    "date": "2024-01-15",
    "type": "shipment",
    "notes": "30 kW"
  }
  
  {
    "customer_id": 1,
    "contact_id": 3,
    "date": "2024-02-20",
    "type": "shipment",
    "notes": "30 kW"
  }
  
  {
    "customer_id": 2,
    "contact_id": 4,
    "date": "2024-03-02",
    "type": "negotiations",
    "notes": "discussing future deals"
  }
  
  {
    "customer_id": 3,
    "contact_id": 5,
    "date": "2024-03-03",
    "type": "discussion at equipment exhibition",
    "notes": "product demonstration"
  }

  GET      localhost:8080/cms/intersctions/5

  GET      localhost:8080/cms/interactions/customers/1

  GET      localhost:8080/cms/interactions

  PUT      localhost:8080/cms/interactions/2
  
  {
    "customer_id": 1,
    "contact_id": 1,
    "date": "2024-01-17",
    "type": "shipment",
    "notes": "50 kW"
  }

  DELETE   localhost:8080/cms/interactions/5
  


  

  

  
