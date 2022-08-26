echo "script start.."

main_container="sns"
path="dockerfiles/docker-compose-dev.yml"


if [ -z `docker-compose -f $path ps -q $main_container` ] || [ -z `docker ps -q --no-trunc | grep $(docker-compose -f $path ps -q $main_container)` ]; then
        echo "container is not running."
        docker-compose -f $path up --build -d
else
        echo "container is running. restart only $main_container, redis"
        docker-compose -f $path stop $main_container redis
        echo "wait 3 seconds..."
        sleep 3
        echo "container remove..."
        docker-compose -f $path rm -f $main_container redis
        echo "container build.."
        docker-compose -f $path up --build -d

        echo "remove all docker caches"
        sudo docker system prune --all -f
fi

echo "restart completed"