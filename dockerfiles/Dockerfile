FROM openjdk:11-jdk
COPY ../ .

ENV USER dltmd202

# root ssh login enable
RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
# /etc/pam.d/sshd disable
RUN sed -i 's/UsePAM yes/#UserPAM yes/g' /etc/ssh/sshd_config

# 999 groupadd groupname:username
RUN groupadd -g 999 $USER
# user add in group
RUN useradd -m -r -u 999 -g $USER $USER

RUN sed -ri '20a'$USER' ALL=(ALL) NOPASSWD:ALL' /etc/sudoers

RUN echo 'root:root' | chpasswd
RUN echo $USER':[userpassword]' | chpasswd

USER $USER

CMD ["./gradlew", "bootRun", "--args='--spring.profiles.active=prod'"] 