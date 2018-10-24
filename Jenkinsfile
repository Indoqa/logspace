/**
Logspace
Copyright (c) 2018 Indoqa Software Design und Beratung GmbH. All rights reserved.
This program and the accompanying materials are made available under the terms of
the Eclipse Public License Version 1.0, which accompanies this distribution and
is available at http://www.eclipse.org/legal/epl-v10.html.
**/
pipeline {
  
  agent any

  parameters {
      booleanParam(name: 'DEPLOY_ARTIFACTS', defaultValue: false, description: 'Deploy artifacts to nexus')
      booleanParam(name: 'RUN_SONAR', defaultValue: false, description: 'Run sonar analysis')
  }

  environment {
    MAVEN_BUILD_PROPERTIES=''
    DEPLOY_BRANCH='master'
  }

  triggers {
    snapshotDependencies()
    parameterizedCron '15 23 * * * % DEPLOY_ARTIFACTS=true;RUN_SONAR=true;CRON=true'
  }

  tools {
    maven 'Maven 3.5.x' 
    nodejs '8.1.2'
  }

  stages {
    stage('Build') {
      options {
        timeout(time: 5, unit: 'MINUTES') 
      }    
      steps {
        sh 'mvn clean install -Ptest-coverage,indoqa-release ${MAVEN_BUILD_PROPERTIES}'
      }
    }

    stage('SonarQube analysis') { 
      when {
        environment name: 'RUN_SONAR', value: 'true'
      }

      steps {
        withSonarQubeEnv('sonar') { 
          sh 'mvn -Ptest-coverage,indoqa-release sonar:sonar -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.password= '
        }
      }
    }

    stage('Deploy to nexus') {
      when {
        allOf {
          branch "${DEPLOY_BRANCH}"
          environment name: 'DEPLOY_ARTIFACTS', value: 'true'
        }
      }
      
      steps {
        sh 'mvn deploy ${MAVEN_BUILD_PROPERTIES}'
      }
    }
  }

  post {
    changed {
      echo "Changed to ${currentBuild.result}"
      script {
        if(currentBuild.resultIsBetterOrEqualTo('SUCCESS')) {
          slackSend channel: '#ci_oss', color: '#008000', tokenCredentialId: 'Slack_IntegrationToken', message: "${env.JOB_NAME} has recovered at ${env.BUILD_NUMBER} status: ${currentBuild.currentResult} (<${env.BUILD_URL}|Open>)"
        }
        if(currentBuild.resultIsWorseOrEqualTo('FAILURE')) {
          slackSend channel: '#ci_oss', color: '#800000', tokenCredentialId: 'Slack_IntegrationToken', message: "${env.JOB_NAME} has failed at ${env.BUILD_NUMBER} status: ${currentBuild.currentResult} (<${env.BUILD_URL}|Open>)"
        }
      }
    }
  }
}

