# selva-oss

**Features:**

* Query generator


***Query generator***

please refer to the QueryTest file for usage information.

**use cases**

* a library similar to react query builder but for use from the backend.
* you can allow users to better filter the data delivered based on multiple filter conditions.
* just get the filters selected by the user and send that to this lib in the required json format you are done, it will generate the query (where condition) which you can use to query the data from the database.

**what it has to offer:**

* supports named fields and constants as arguments.
* requires you to define the type of every named field involved in the query.
* makes sure that every comparative condition has arguments of the same type.
* nest and, or, not infinitly (if physically possible ;))
* supports the following conditions:- =,!=, >, >=, <,<=
* supports the following data types:- String, Integer, Long, Float, Double

**note:**

* only equals and not equals are supported for String.
* Numerical data types support all the conditions.
* a single condition cannot have both of its arguments as constants.

**in development:**

* converting to sql template for using with jdbc.
* composite conditions
* customizable templates

 If you find this useful I am more than happy to develop more useful features on top of it, the following are some ideas for feature requests.
 
 * customize the representation of the conditions in the query output.
 * customize the representation of the constants and named fields in the query output.
 * support more data types and conditions.



Thank you ❤️

