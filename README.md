# selva-oss

**Features:**

* Query generator


***Query generator***

please refer to the QuerTest file for usage information.

**what it has to offer:**

* supports named fields and constants as arguments.
* requires you to define the type of every named field involved in the query.
* makes sure that every comparative condition has arguments of the same type.
* nest and, or, not infinity (if physically possible ;))
* supports the following conditions:- =,!=, >, >=, <,<=
* supports the following data types:- String, Integer, Long, Float, Double

**note:**

* only equals and not equals are supported for String.
* Numerical data types support all the conditions.
* a single condition cannot have both of its arguments as constants.

 If you find this useful I am more than happy to develop more useful features on top of it, the following are some ideas for feature requests.
 
 * customize the representation of the conditions in the query output.
 * customize the representation of the constants and named fields in the query output.

Thank you ❤️

