import json
import os

DIR = os.path.dirname(os.path.abspath(__file__))
SENSORS_DIR = os.path.join(DIR, "sensors")


class Register:

    @staticmethod
    def write(data, sensor):
        """Enregistre les données dans un fichier"""
        print(data)
        print(os.path.join(SENSORS_DIR, sensor + ".json"))
        file = open(os.path.join(SENSORS_DIR, sensor + ".json"), "w")
        file.write(data)
        file.close()

    @staticmethod
    def read(sensor):
        """Lit les données d'un fichier"""
        file = open(os.path.join(SENSORS_DIR, sensor + ".json"), "r")
        data = file.read()
        file.close()
        return data

    @staticmethod
    def get_files_name():
        """Retourne la liste des fichiers"""
        files = os.listdir(SENSORS_DIR)
        for i in range(len(files)):
            files[i] = files[i].replace(".json", "")
        return files

    @staticmethod
    def file_exists(sensor):
        """Vérifie si le fichier existe"""
        try:
            open(os.path.join(SENSORS_DIR, sensor + ".json"), "r")
            return True
        except IOError:
            return False

    @staticmethod
    def create_file(sensor):
        """Crée un fichier"""
        Register.write(json.dumps([]), sensor)

    @staticmethod
    def get_sensors(sensor):
        """Retourne les données d'un capteur"""
        if Register.file_exists(sensor):
            return json.loads(Register.read(sensor))
        else:
            Register.create_file(sensor)
            return json.loads(Register.read(sensor))

    @staticmethod
    def get_all_sensors():
        """Retourne les données de tous les capteurs"""
        sensors = []
        for sensor in Register.get_sensors("sensors"):
            sensors.append(sensor)
        return sensors

    @staticmethod
    def get_last_value(sensor):
        """Retourne la dernière valeur d'un capteur"""
        datas = Register.get_sensors(sensor)
        if len(datas) > 0:
            return datas[-1]
        else:
            return None

    @staticmethod
    def get_all_sensors_last_value():
        """Retourne la dernière valeur de tous les capteurs"""
        sensors = []
        for sensor in Register.get_files_name():
            last_value = Register.get_last_value(sensor)
            if last_value is not None:
                sensors.append(last_value)
        return sensors

    @staticmethod
    def add_sensors_data(sensor, data):
        """Ajoute des données à un capteur"""
        datas = Register.get_sensors(sensor)
        datas.append(data)
        Register.write(json.dumps(datas), sensor)

