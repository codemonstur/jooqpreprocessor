
[![Build Status](https://travis-ci.org/codemonstur/jooqpreprocessor.svg?branch=master)](https://travis-ci.org/codemonstur/jooqpreprocessor)
[![GitHub Release](https://img.shields.io/github/release/codemonstur/jooqpreprocessor.svg)](https://github.com/codemonstur/jooqpreprocessor/releases) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.codemonstur/jooqpreprocessor/badge.svg)](http://mvnrepository.com/artifact/com.github.codemonstur/jooqpreprocessor)
[![Maintainability](https://api.codeclimate.com/v1/badges/1d01c5c6574dcbd6c359/maintainability)](https://codeclimate.com/github/codemonstur/jooqpreprocessor/maintainability)
[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)
[![Coverage Status](https://coveralls.io/repos/github/codemonstur/jooqpreprocessor/badge.svg?branch=master)](https://coveralls.io/github/codemonstur/jooqpreprocessor?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d51906afb14344b781ac90e3ae6f598c)](https://www.codacy.com/manual/codemonstur/jooqpreprocessor)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/codemonstur/jooqpreprocessor)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)

## JOOQ preprocessor

Almost functional.

The jOOQ code generator is missing various features that prevents its use in a fully automated way.
The standard workaround is to maintain two lists of SQL scripts; one for the DB to do the migration and one for the code generator to work on.

Alternatively you can try rewriting your migration scripts to be parsable by both.
However there are some features that are really hard to get around because of limitations in the parser.

This preprocessor seeks to make development easier by taking the migration scripts and converting them to what the parser can handle.
The parser can then operate on the reduced set of instructions.

Its been a bit of journey to find all the quirks.
And the code has rough edges and is lacking tests.
But it is getting very close to doing its job.