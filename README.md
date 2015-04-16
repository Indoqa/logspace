# Indoqa logspace.io

## Prepare the frontend development environment

### Mac

- Install node.js: ```brew install node```

### Windows

- Install [node.js](https://nodejs.org/download/)
- Install [Python 2.7.x](https://www.python.org/downloads/) and add it to your path or/and create a PYTHONPATH environment variable.
- Install Visual Studio, the [Express Edition](https://www.visualstudio.com/en-us/products/visual-studio-express-vs.aspx) is fine.
- Set GYP_MSVS_VERSION environment property, if you are using Express, ```GYP_MSVS_VERSION=2013e```

### Linux (Debian based)

- Install node.js: ```curl -sL https://deb.nodesource.com/setup | sudo bash -```
- Install build essentials and C++ compiler: ```sudo apt-get install build-essential g++```

## Install node dependencies
``` shell
npm install -g gulp
cd logspace-frontend
npm install
```

## Run the frontend

The logspace frontend uses [este.js](https://github.com/steida/este) as development stack. este.js integrates all necessary tools to have an environment for convenient Javascript (Ecmascript 6) development (automatic reloads of Javascript and CSS, support of multiple CSS pre-compilers, etc.) and provides tools to create of artifacts (html/js/css).

### Start development environment
``` shell
cd logspace-frontend
gulp
```

Now point your browser to [localhost:8000](http://localhost:8000)


### Build release artifacts
``` shell
cd logspace-frontend
gulp -p build
```

The artifacts are located in ```logspace-frontend/build```
