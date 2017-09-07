#!/usr/bin/env bash

set -ex

cd deploy
bundle install --deployment
bundle exec rake delete
bundle exec rake deploy

