### Problem definition
Create a tiny RESTful web service with the following functionality:

- Application must expose REST API endpoints for the following functionality:
  - apply for loan ('loan amount', 'term', 'name', 'surname' and 'personal_id' must be provided)
  - list all approved loans
  - list all approved loans by user
- Service must perform loan application validation according to the following rules and reject application if:
  - Application comes from blacklisted personal id
  - N application / second are received from a single country (essentially we want to limit number of loan application coming from country)
- Service must perform origin country resolution using a web service and store  country code together with the loan application. Because network is unreliable and services tend to fail, let's agree on default country code - "lv".

### Technical requirements

You have total control over framework and tools, as long as application is written in Java.
  