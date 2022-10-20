
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import javax.swing.Timer;
import javax.swing.table.TableModel;

/**
 *
 * @author Carl Eric Doromal
 */
public class SRTF extends javax.swing.JFrame {  
    
    private boolean resuming = false;
    private boolean finished = false;
    private int time = 0, totalFinished = 0;
    private Job currJob = null;
    private Job[] jobList = null;
    
    private PriorityQueue<Job> arrivalQ = new PriorityQueue(5, new Comparator<Job>() {
        @Override
        public int compare(Job job1, Job job2) {
            return job1.getArrive() - job2.getArrive();
        }
    });
    
    private PriorityQueue<Job> readyQ = new PriorityQueue(5, new Comparator<Job>() {
        @Override
        public int compare(Job job1, Job job2) {
            return job1.getBurst() - job2.getBurst();
        }
    });
    
    private Timer timer = null;
    
    private void resetState() {
        time = 0;
        totalFinished = 0;
        currJob = null;
        jobList = null;
        arrivalQ.clear();
        readyQ.clear();
        resuming = false;
        finished = false;
        ganttMonitor.removeAll();
        ganttMonitor.revalidate();
        ganttMonitor.repaint();
        queueMonitor.removeAll();
        queueMonitor.revalidate();
        queueMonitor.repaint();
        jobsTable.setEnabled(true);
        currJobMonitor.setText("idle");
        currTimeMonitor.setText("0 -> 1");
        waitTimeMonitor.setText("0.0");
        taTimeMonitor.setText("0.0");
    }
    
    private void pause() {
        timer.stop();
        startButton.setEnabled(true);
        stepButton.setEnabled(true);
        pauseButton.setEnabled(false);
    }
    
    private void loadJobs() {
        TableModel jobsTableModel = jobsTable.getModel();
        
        //load jobs to arrivalQ
        for (int row = 0; row < 5; row++) {
            if ((boolean)jobsTableModel.getValueAt(row, 3)) {
                arrivalQ.add(new Job(jobsTableModel.getValueAt(row, 0) == null ? "P"+(row+1) : (String)jobsTableModel.getValueAt(row, 0), 
                jobsTableModel.getValueAt(row, 1) == null ? 0 : (int)jobsTableModel.getValueAt(row, 1), 
                jobsTableModel.getValueAt(row, 2) == null ? 1 : (int)jobsTableModel.getValueAt(row, 2)));
            }
        }
        jobList = arrivalQ.toArray(new Job[0]);
    }
    
    private void updateQueueMonitor() {
        queueMonitor.removeAll();
        Iterator iterator = readyQ.iterator();
        while (iterator.hasNext()) {
            queueMonitor.add(new ProcessCell((Job)iterator.next(), 32, 70));
        }
        queueMonitor.revalidate();
        queueMonitor.repaint();
    }
    
    private void nextStep() {
        if (finished) {
            resetState();
        }
        
        if (!resuming) {
            loadJobs();
            jobsTable.setEnabled(false);
            resuming = true;
        }
        
        //get newly arrived job
        if (!arrivalQ.isEmpty() && time >= arrivalQ.peek().getArrive()) {
            readyQ.add(arrivalQ.remove());
        }
        
        //fill currJob
        if (currJob == null) {
            if (readyQ.isEmpty()) {
                currJob = new Job("idle", 0, 1);
            } else {
                currJob = readyQ.remove();
            }
        }
        
        //swap to lowest remaining time
        if (!readyQ.isEmpty() && currJob.getBurst() > readyQ.peek().getBurst()) {
            Job temp = currJob;
            currJob = readyQ.remove();
            readyQ.add(temp);
        }
        
        //increment wait counters in readyQ
        Iterator iterator = readyQ.iterator();
        while (iterator.hasNext()) {
            ((Job)iterator.next()).incWait();
        }
        
        //update visuals
        ganttMonitor.add(new ProcessCell(currJob, 20, 50));
        ganttMonitor.revalidate();
        ganttMonitor.repaint();
        updateQueueMonitor();
        currJobMonitor.setText(currJob.getName());
        currTimeMonitor.setText(time + " -> " + ++time);
        currJob.decBurst();
        if (currJob.getBurst() == 0) {
            if(!currJob.getName().equals("idle")) {
                totalFinished++;
                taTimeMonitor.setText(String.valueOf((Double.parseDouble(taTimeMonitor.getText())+(time-currJob.getArrive()))/totalFinished));
            }
            //clear currJob
            currJob = null;
        }
        
        int totalWait = 0;
        for (Job job : jobList) {
            totalWait += job.getWait();
            waitTimeMonitor.setText(String.valueOf((double)totalWait/jobList.length));
        }
        
        //stop if finished
        if (arrivalQ.isEmpty() && readyQ.isEmpty() && currJob == null) {
            pause();
            finished = true;
            jobsTable.setEnabled(true);
        }
    }
    
    /**
     * Creates new form SRTF
     */
    public SRTF() {
        initComponents();
        timer = new Timer(1000/speedSlider.getValue(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextStep();
            }
        });
        timer.setInitialDelay(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        srtfPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        controlLabel = new javax.swing.JLabel();
        speedLabel = new javax.swing.JLabel();
        speedSlider = new javax.swing.JSlider();
        startButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        stepButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jobsPanel = new javax.swing.JPanel();
        jobsLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jobsTable = new javax.swing.JTable();
        cpuPanel = new javax.swing.JPanel();
        cpuLabel = new javax.swing.JLabel();
        currJobLabel = new javax.swing.JLabel();
        currJobMonitor = new javax.swing.JLabel();
        currTimeLabel = new javax.swing.JLabel();
        currTimeMonitor = new javax.swing.JLabel();
        queuePanel = new javax.swing.JPanel();
        queueLabel = new javax.swing.JLabel();
        queueMonitor = new javax.swing.JPanel();
        averagePanel = new javax.swing.JPanel();
        averageLabel = new javax.swing.JLabel();
        waitTimeLabel = new javax.swing.JLabel();
        waitTimeMonitor = new javax.swing.JLabel();
        taTimeLabel = new javax.swing.JLabel();
        taTimeMonitor = new javax.swing.JLabel();
        ganttPanel = new javax.swing.JPanel();
        ganttLabel = new javax.swing.JLabel();
        ganttMonitor = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SRTF");
        setPreferredSize(new java.awt.Dimension(710, 550));
        getContentPane().setLayout(new java.awt.CardLayout());

        java.awt.GridBagLayout srtfPanelLayout = new java.awt.GridBagLayout();
        srtfPanelLayout.columnWidths = new int[] {0, 5, 0, 5, 0};
        srtfPanelLayout.rowHeights = new int[] {0, 5, 0, 5, 0};
        srtfPanel.setLayout(srtfPanelLayout);

        controlPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        controlPanel.setPreferredSize(new java.awt.Dimension(100, 100));

        controlLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        controlLabel.setText("CONTROLS");

        speedLabel.setText("Speed");

        speedSlider.setMajorTickSpacing(1);
        speedSlider.setMaximum(50);
        speedSlider.setMinimum(1);
        speedSlider.setValue(25);
        speedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                speedSliderStateChanged(evt);
            }
        });

        startButton.setText("Start");
        startButton.setPreferredSize(new java.awt.Dimension(100, 22));
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        pauseButton.setText("Pause");
        pauseButton.setEnabled(false);
        pauseButton.setPreferredSize(new java.awt.Dimension(100, 22));
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        stepButton.setText("Next Step");
        stepButton.setPreferredSize(new java.awt.Dimension(100, 22));
        stepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepButtonActionPerformed(evt);
            }
        });

        resetButton.setText("Reset");
        resetButton.setPreferredSize(new java.awt.Dimension(100, 22));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(controlLabel)
                    .addGroup(controlPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(controlPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(speedLabel)
                            .addGroup(controlPanelLayout.createSequentialGroup()
                                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stepButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(controlPanelLayout.createSequentialGroup()
                                .addComponent(pauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controlLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(speedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stepButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        srtfPanel.add(controlPanel, gridBagConstraints);

        jobsPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jobsPanel.setPreferredSize(new java.awt.Dimension(100, 100));

        jobsLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jobsLabel.setText("JOBS");

        jobsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"P1",  new Integer(0),  new Integer(1),  new Boolean(true)},
                {"P2",  new Integer(0),  new Integer(1),  new Boolean(true)},
                {"P3",  new Integer(0),  new Integer(1),  new Boolean(true)},
                {"P4",  new Integer(0),  new Integer(1),  new Boolean(true)},
                {"P5",  new Integer(0),  new Integer(1),  new Boolean(true)}
            },
            new String [] {
                "Name", "Arrival Time", "Burst Time", "Enabled"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jobsTable);
        if (jobsTable.getColumnModel().getColumnCount() > 0) {
            jobsTable.getColumnModel().getColumn(0).setResizable(false);
            jobsTable.getColumnModel().getColumn(1).setResizable(false);
            jobsTable.getColumnModel().getColumn(2).setResizable(false);
            jobsTable.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jobsPanelLayout = new javax.swing.GroupLayout(jobsPanel);
        jobsPanel.setLayout(jobsPanelLayout);
        jobsPanelLayout.setHorizontalGroup(
            jobsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jobsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jobsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jobsPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jobsPanelLayout.createSequentialGroup()
                        .addComponent(jobsLabel)
                        .addGap(0, 423, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jobsPanelLayout.setVerticalGroup(
            jobsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jobsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jobsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        srtfPanel.add(jobsPanel, gridBagConstraints);

        cpuPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        cpuPanel.setPreferredSize(new java.awt.Dimension(100, 100));

        cpuLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cpuLabel.setText("CPU");

        currJobLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        currJobLabel.setText("Current Job");

        currJobMonitor.setText("Idle");

        currTimeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        currTimeLabel.setText("Current Time");

        currTimeMonitor.setText("0 -> 1");

        javax.swing.GroupLayout cpuPanelLayout = new javax.swing.GroupLayout(cpuPanel);
        cpuPanel.setLayout(cpuPanelLayout);
        cpuPanelLayout.setHorizontalGroup(
            cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cpuLabel)
                    .addGroup(cpuPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currJobLabel)
                            .addComponent(currTimeLabel)
                            .addGroup(cpuPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(currJobMonitor)
                                    .addComponent(currTimeMonitor))))))
                .addContainerGap(145, Short.MAX_VALUE))
        );
        cpuPanelLayout.setVerticalGroup(
            cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cpuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cpuLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currJobLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currJobMonitor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currTimeMonitor)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        srtfPanel.add(cpuPanel, gridBagConstraints);

        queuePanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        queuePanel.setPreferredSize(new java.awt.Dimension(100, 100));

        queueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        queueLabel.setText("QUEUE");

        queueMonitor.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));

        javax.swing.GroupLayout queuePanelLayout = new javax.swing.GroupLayout(queuePanel);
        queuePanel.setLayout(queuePanelLayout);
        queuePanelLayout.setHorizontalGroup(
            queuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(queuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(queuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(queuePanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(queueMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(queuePanelLayout.createSequentialGroup()
                        .addComponent(queueLabel)
                        .addGap(0, 174, Short.MAX_VALUE)))
                .addContainerGap())
        );
        queuePanelLayout.setVerticalGroup(
            queuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(queuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(queueLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(queueMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        srtfPanel.add(queuePanel, gridBagConstraints);

        averagePanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        averagePanel.setPreferredSize(new java.awt.Dimension(100, 100));

        averageLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        averageLabel.setText("AVERAGE");

        waitTimeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        waitTimeLabel.setText("Wait Time");

        waitTimeMonitor.setText("0.0");

        taTimeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        taTimeLabel.setText("Turn Around Time");

        taTimeMonitor.setText("0.0");

        javax.swing.GroupLayout averagePanelLayout = new javax.swing.GroupLayout(averagePanel);
        averagePanel.setLayout(averagePanelLayout);
        averagePanelLayout.setHorizontalGroup(
            averagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(averagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(averagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(averageLabel)
                    .addGroup(averagePanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(averagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(waitTimeLabel)
                            .addComponent(taTimeLabel)
                            .addGroup(averagePanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(averagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(waitTimeMonitor)
                                    .addComponent(taTimeMonitor))))))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        averagePanelLayout.setVerticalGroup(
            averagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(averagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(averageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(waitTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(waitTimeMonitor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(taTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(taTimeMonitor)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        srtfPanel.add(averagePanel, gridBagConstraints);

        ganttPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        ganttPanel.setPreferredSize(new java.awt.Dimension(100, 100));

        ganttLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ganttLabel.setText("GANTT CHART");

        ganttMonitor.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 5));

        javax.swing.GroupLayout ganttPanelLayout = new javax.swing.GroupLayout(ganttPanel);
        ganttPanel.setLayout(ganttPanelLayout);
        ganttPanelLayout.setHorizontalGroup(
            ganttPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ganttPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ganttPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ganttPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(ganttMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ganttPanelLayout.createSequentialGroup()
                        .addComponent(ganttLabel)
                        .addGap(0, 597, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ganttPanelLayout.setVerticalGroup(
            ganttPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ganttPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ganttLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ganttMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        srtfPanel.add(ganttPanel, gridBagConstraints);

        getContentPane().add(srtfPanel, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        startButton.setEnabled(false);
        stepButton.setEnabled(false);
        timer.start();
        pauseButton.setEnabled(true);
    }//GEN-LAST:event_startButtonActionPerformed

    private void stepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepButtonActionPerformed
        if (!finished) nextStep();
    }//GEN-LAST:event_stepButtonActionPerformed

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        pause();
    }//GEN-LAST:event_pauseButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        pause();
        resetState();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void speedSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_speedSliderStateChanged
        timer.setDelay(1000/speedSlider.getValue());
    }//GEN-LAST:event_speedSliderStateChanged
 
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SRTF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SRTF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SRTF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SRTF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SRTF().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel averageLabel;
    private javax.swing.JPanel averagePanel;
    private javax.swing.JLabel controlLabel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel cpuLabel;
    private javax.swing.JPanel cpuPanel;
    private javax.swing.JLabel currJobLabel;
    private javax.swing.JLabel currJobMonitor;
    private javax.swing.JLabel currTimeLabel;
    private javax.swing.JLabel currTimeMonitor;
    private javax.swing.JLabel ganttLabel;
    private javax.swing.JPanel ganttMonitor;
    private javax.swing.JPanel ganttPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jobsLabel;
    private javax.swing.JPanel jobsPanel;
    private javax.swing.JTable jobsTable;
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel queueLabel;
    private javax.swing.JPanel queueMonitor;
    private javax.swing.JPanel queuePanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JPanel srtfPanel;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stepButton;
    private javax.swing.JLabel taTimeLabel;
    private javax.swing.JLabel taTimeMonitor;
    private javax.swing.JLabel waitTimeLabel;
    private javax.swing.JLabel waitTimeMonitor;
    // End of variables declaration//GEN-END:variables
}
