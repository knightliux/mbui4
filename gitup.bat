@echo off
echo �����ύ��ע:
set /p commit=
set gitPath="C:\Program Files\Git\cmd\"
::echo ��ǰ�̷���·����%~dp0
if not exist .git (
  echo �״��ύ�����ύ��ַ:
  set /p url=
)
if not exist .git (
 ::echo ������
 echo �ύ��ַ:%url%
 pause
 %gitPath%git init
 %gitPath%git remote add origin "%url%"
)

%gitPath%git pull origin master
%gitPath%git add -A
%gitPath%git commit -m "%commit%"
%gitPath%git push origin master 
pause